 package bean;

public class JavManifestSent{
    public JavManifestSent(){}
    String manifestNumber;
    int guiaNumber;
    int noOfPackages;
    double manifestAmount;
    String manifestStatus;
    String preferredCollectionTime;

    public String getManifestNumber(){
        return manifestNumber;
    }
    public void setManifestNumber(String manifestNumber){
        this.manifestNumber = manifestNumber;
    }

    public int getGuiaNumber(){
        return guiaNumber;
    }
    public void setGuiaNumber(int guiaNumber){
        this.guiaNumber = guiaNumber;
    }

    public int getNoOfPackages(){
        return noOfPackages;
    }
    public void setNoOfPackages(int noOfPackages){
        this.noOfPackages = noOfPackages;
    }

    public double getManifestAmount(){
        return manifestAmount;
    }
    public void setManifestAmount(double manifestAmount){
        this.manifestAmount = manifestAmount;
    }

    public String getManifestStatus(){
        return manifestStatus;
    }
    public void setManifestStatus(String manifestStatus){
        this.manifestStatus = manifestStatus;
    }

    public String getPreferredCollectionTime(){
        return preferredCollectionTime;
    }
    public void setPreferredCollectionTime(String preferredCollectionTime){
        this.preferredCollectionTime = preferredCollectionTime;
    }
}
