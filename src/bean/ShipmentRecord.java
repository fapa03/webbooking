 package bean;
public class ShipmentRecord{
  private String quantity;
  private String description;
  private String content;
  private String peso;
  private String volume;
  private String charges;
  public ShipmentRecord(){}

  public String getQuantity(){
    return quantity;
  }
  public String getDescription(){
    return description;
  }
  public String getContent(){
    return content;
  }
  public String getPeso(){
    return peso;
  }
  public String getVolume(){
    return volume;
  }
  public String getCharges(){
    return charges;
  }
  public void setQuantity(String quantity){
    this.quantity= quantity;
  }
  public void setDescription(String description){
    this.description = description;
  }
  public void setContent(String content){
    this.content = content;
  }
  public void setPeso(String peso){
    this.peso = peso;
  }
  public void setVolume(String volume){
    this.volume = volume;
  }
  public void setCharges(String charges){
    this.charges= charges;
  }
}
