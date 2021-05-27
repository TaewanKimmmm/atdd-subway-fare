package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicateColorException;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationService stationService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine;

        if (lineDao.isExistByName(request.getName())) {
            throw new DuplicateNameException("이미 존재하는 노선입니다.");
        }

        if (lineDao.isExistByColor(request.getColor())) {
            throw new DuplicateColorException("이미 존재하는 노선 색깔입니다.");
        }

        persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        persistLine.addSection(addInitSection(persistLine, request));
        return LineResponse.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequest request) {
        if (request.getUpStationId() != null && request.getDownStationId() != null) {
            Station upStation = stationService.findStationById(request.getUpStationId());
            Station downStation = stationService.findStationById(request.getDownStationId());
            Section section = new Section(upStation, downStation, request.getDistance());
            return sectionDao.insert(line, section);
        }
        return null;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        try {
            return lineDao.findById(id);
        } catch (RuntimeException e) {
            throw new NoSuchElementException("존재하지 않는 노선입니다.");
        }
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        if (lineDao.isExistByName(lineUpdateRequest.getName())) {
            throw new DuplicateNameException("이미 존재하는 노선입니다.");
        }

        if (lineDao.isExistByColor(lineUpdateRequest.getColor())) {
            throw new DuplicateColorException("이미 존재하는 노선 색깔입니다.");
        }
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getExtraFare()));
    }

    public void deleteLineById(Long id) {
        int affectedRow = lineDao.deleteById(id);
        if (affectedRow == 0) {
            throw new NoSuchElementException("존재하지 않는 노선입니다.");
        }
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }
}
