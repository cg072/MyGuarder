package home.safe.com.notice;

/**
 * Created by plupin724 on 2017-11-16.
 *
 * 테스트를 위한 dto 생성
 */

public class TestDtoNotice {

    String testtitle;
    String testauthor;
    String testcontents;

    public TestDtoNotice(String testtitle, String testauthor, String testcontents) {
        this.testtitle = testtitle;
        this.testauthor = testauthor;
        this.testcontents = testcontents;
    }

    public String getTesttitle() {
        return testtitle;
    }

    public void setTesttitle(String testtitle) {
        this.testtitle = testtitle;
    }

    public String getTestauthor() {
        return testauthor;
    }

    public void setTestauthor(String testauthor) {
        this.testauthor = testauthor;
    }

    public String getTestcontents() {
        return testcontents;
    }

    public void setTestcontents(String testcontents) {
        this.testcontents = testcontents;
    }
}
