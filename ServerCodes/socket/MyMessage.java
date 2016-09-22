public class MyMessage {
    public String id;
    public String password = null;
    public String destId;
    public String content;
    public String type = "message";
    public String localType = "frd";
    public String bitmapString = null;


    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDestId() {
        return destId;
    }

    public String getType() {
        return type;
    }
	
    public String toString(){
	return "id="+id+",destId="+destId+",type="+type+",content="+content;
    }
}
