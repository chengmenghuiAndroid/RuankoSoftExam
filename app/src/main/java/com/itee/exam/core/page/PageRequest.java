/**
 *
 */
package com.itee.exam.core.page;

import java.util.List;

/**
 * @author xin
 */
public class PageRequest {

    private int page = 1;
    private int pageSize = 20;
    private int take;
    private int skip;

    protected FilterDescriptor filter;

    public PageRequest() {
        setPageSize(20);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.skip = (page - 1) * pageSize;
        if (this.skip < 0) {
            this.skip = 0;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.take = pageSize;
        this.skip = (page - 1) * pageSize;
        if (this.skip < 0) {
            this.skip = 0;
        }
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public FilterDescriptor getFilter() {
        return filter;
    }

    public void setFilter(FilterDescriptor filter) {
        this.filter = filter;
    }

    public static class FilterDescriptor {

        public static final String LOGIC_AND = "and";

        public static final String OPERATOR_CONTAINS = "contains";
        public static final String OPERATOR_EQ = "eq";
        public static final String OPERATOR_NEQ = "neq";

        private String logic;
        private List<FilterDescriptor> filters;
        private String field;
        private Object value;
        private String operator;
        private boolean ignoreCase = true;

        public FilterDescriptor() {
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getLogic() {
            return logic;
        }

        public void setLogic(String logic) {
            this.logic = logic;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }

        public void setFilters(List<FilterDescriptor> filters) {
            this.filters = filters;
        }

        public List<FilterDescriptor> getFilters() {
            return filters;
        }
    }

}
