package fr.poei.open.proxybanque.dtos;

public class ResponseBodyDto {
    private String response;

    public ResponseBodyDto(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
