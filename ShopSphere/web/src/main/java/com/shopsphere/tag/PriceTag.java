package com.shopsphere.tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * RTU Syllabus Core Requirement: Classic Tag Extension (Tag Handler)
 *
 * Demonstrates a custom JSP tag handler extending SimpleTagSupport.
 * Formats currency amounts with locale-sensitive formatting and optional strikethrough styling.
 *
 * SimpleTag Handler Lifecycle in Jakarta EE:
 * 1. Instantiation: Container invokes default zero-argument constructor.
 * 2. setJspContext(JspContext pc): Container provides the invoking page context.
 * 3. setParent(JspTag parent): Container sets enclosing parent tag (if nested).
 * 4. Property Setters: Container calls setters for each tag attribute (amount, currency, strikethrough).
 * 5. setJspBody(JspFragment body): Container passes body fragment (if tag has body).
 * 6. doTag(): Primary execution method invoked to output content or process body.
 */
public class PriceTag extends SimpleTagSupport {

    private BigDecimal amount;
    private String currency = "₹";
    private boolean strikethrough = false;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        if (currency != null && !currency.trim().isEmpty()) {
            this.currency = currency.trim();
        }
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formattedNumber = df.format(amount);

        StringBuilder sb = new StringBuilder();
        if (strikethrough) {
            sb.append("<span class=\"text-muted text-decoration-line-through\">");
            sb.append(currency).append(formattedNumber);
            sb.append("</span>");
        } else {
            sb.append("<span class=\"fw-bold text-primary\">");
            sb.append(currency).append(formattedNumber);
            sb.append("</span>");
        }

        out.print(sb.toString());
    }
}
