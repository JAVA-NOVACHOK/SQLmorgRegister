
package artem.nikname.demoSQLSpringHTML.model;


public class SearchClass {
    
    private String searchName;
    private String searchSurname;
    private String yearFrom;
    private String yearTo;

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchSurname() {
        return searchSurname;
    }

    public void setSearchSurname(String searchSurname) {
        this.searchSurname = searchSurname;
    }

    public String getYearFrom() {
        return yearFrom + ",01,01";
    }

    public void setYearFrom(String yearFrom) {
        this.yearFrom = yearFrom;
    }

    public String getYearTo() {
        return yearTo + ",12,31";
    }

    public void setYearTo(String yearTo) {
        this.yearTo = yearTo;
    }

    @Override
    public String toString() {
        return "SearchClass{" + "searchName=" + searchName + 
                ", searchSurname=" + searchSurname + ", yearFrom=" + 
                yearFrom + ", yearTo=" + yearTo + '}';
    }
    
    
    
    
    
}
