import java.util.*;
public class Info{
    private String id = null;
    private String result = "fail";
    private List<Friend> friends = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}
