package org.estatio.dom.lease;

import java.math.BigDecimal;

import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.InheritanceStrategy;

import org.estatio.dom.utils.MathUtils;
import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;

@javax.jdo.annotations.PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@javax.jdo.annotations.Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class LeaseTermForServiceCharge extends LeaseTerm {

    // {{ BudgetedValue (property)
    @javax.jdo.annotations.Column(scale = 2)
    private BigDecimal budgetedValue;

    @MemberOrder(sequence = "11", name = "Service Charges")
    public BigDecimal getBudgetedValue() {
        return budgetedValue;
    }

    public void setBudgetedValue(final BigDecimal budgetedValue) {
        this.budgetedValue = budgetedValue;
    }

    // }}

    // {{ AuditedValue (property)
    @javax.jdo.annotations.Column(scale = 2)
    private BigDecimal auditedValue;

    @MemberOrder(sequence = "12", name = "Service Charges")
    public BigDecimal getAuditedValue() {
        return auditedValue;
    }

    public void setAuditedValue(final BigDecimal auditedValue) {
        this.auditedValue = auditedValue;
    }

    // }}

    @Override
    public LeaseTerm approve() {
        super.approve();
        return this;
    }

    public String disableApprove() {
        return getStatus().equals(LeaseItemStatus.APPROVED) ? "Already approved" : null;
    }

    @Override
    @Hidden
    public BigDecimal valueForDueDate(LocalDate dueDate) {
        // use the audited value after the end of the term and only when its
        // available
        if (MathUtils.isNotZeroOrNull(getAuditedValue())) {
            if (getEndDate() != null) {
                if (dueDate.compareTo(getEndDate().plusDays(1)) >= 0)
                    return getAuditedValue();
            }
        }
        return getBudgetedValue();
    }

    @Override
    @Hidden
    public void initialize() {
        super.initialize();
        LeaseTermForServiceCharge previousTerm = (LeaseTermForServiceCharge) getPreviousTerm();
        if (previousTerm != null) {
            this.setBudgetedValue(MathUtils.isNotZeroOrNull(previousTerm.getAuditedValue()) ? previousTerm.getAuditedValue() : previousTerm.getBudgetedValue());
        }
    }

    @Override
    @Hidden
    public void update() {
        super.update();
        if (getStatus() == LeaseTermStatus.NEW) {
            // date from previous term
            if (getPreviousTerm() != null && MathUtils.isZeroOrNull(getBudgetedValue())) {
                if (MathUtils.isNotZeroOrNull(getPreviousTerm().getValue())) {
                    setBudgetedValue(getPreviousTerm().getValue());
                }
            }
            // update itself
            if (MathUtils.isNotZeroOrNull(getAuditedValue())) {
                setValue(getAuditedValue());
            } else {
                if (MathUtils.isNotZeroOrNull(getBudgetedValue())) {
                    setValue(getBudgetedValue());
                }
            }
        }
    }

}
