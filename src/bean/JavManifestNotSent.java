 package bean;

public class JavManifestNotSent{
	public JavManifestNotSent(){}
	String manifestNotSentNumber;
    int guiaNotSentNumber;
    int noOfNotSentPackages;
    double manifestNotSentAmount;
    String manifestNotSentStatus;
    String preferredCollectionNotSentTime;
    String docuType;
    public String getDocuType() {
		return docuType;
	}
	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}
	public String getManifestNotSentNumber(){
        return manifestNotSentNumber;
    }
    public void setManifestNotSentNumber(String manifestNotSentNumber){
        this.manifestNotSentNumber = manifestNotSentNumber;
    }

    public int getGuiaNotSentNumber(){
        return guiaNotSentNumber;
    }
    public void setGuiaNotSentNumber(int guiaNotSentNumber){
        this.guiaNotSentNumber = guiaNotSentNumber;
    }

    public int getNoOfNotSentPackages(){
        return noOfNotSentPackages;
    }
    public void setNoOfNotSentPackages(int noOfNotSentPackages){
        this.noOfNotSentPackages = noOfNotSentPackages;
    }

    public double getManifestNotSentAmount(){
        return manifestNotSentAmount;
    }
    public void setManifestNotSentAmount(double manifestNotSentAmount){
        this.manifestNotSentAmount = manifestNotSentAmount;
    }

    public String getManifestNotSentStatus(){
        return manifestNotSentStatus;
    }
    public void setManifestNotSentStatus(String manifestNotSentStatus){
        this.manifestNotSentStatus = manifestNotSentStatus;
    }

    public String getPreferredCollectionNotSentTime(){
        return preferredCollectionNotSentTime;
    }
    public void setPreferredCollectionNotSentTime(String preferredCollectionNotSentTime){
        this.preferredCollectionNotSentTime = preferredCollectionNotSentTime;
    }
}
