package wooteco.subway.line;

import org.springframework.beans.BeanWrapperImpl;
import wooteco.subway.line.dto.SectionRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SectionRequestValidator implements ConstraintValidator<SectionRequestConstraint, SectionRequest> {
    private String upStationId;
    private String downStationId;

    @Override
    public void initialize(SectionRequestConstraint constraintAnnotation) {
        this.upStationId = constraintAnnotation.upStationId();
        this.downStationId = constraintAnnotation.downStationId();
    }

    @Override
    public boolean isValid(SectionRequest sectionRequest, ConstraintValidatorContext context) {
        Object upStationValue = new BeanWrapperImpl(sectionRequest).getPropertyValue("upStationId");
        Object downStationValue = new BeanWrapperImpl(sectionRequest).getPropertyValue("downStationId");
        return !upStationValue.equals(downStationValue);
    }
}