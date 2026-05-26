package bean;

public class JavManifestRecord {

	private String formNumber;
	private String guiaNumber;
	private String destClientName;
	private String guiaAmount;
	private String numPack;
	private String destBranch;
	private String branchLocType;
	private String origBranch;


	public void setFormNumber(String fno) {
		this.formNumber = fno;
	}

	public String getFormNumber() {
		return this.formNumber;
	}

	public void setGuiaNumber(String gno) {
		this.guiaNumber = gno;
	}

	public String getGuiaNumber() {
		return this.guiaNumber;
	}

	public void setDestClientName(String dcn) {
		this.destClientName = dcn;
	}

	public String getDestClientName() {
		return this.destClientName;
	}

	public void setGuiaAmount(String amt) {
		this.guiaAmount = amt;
	}

	public String getGuiaAmount() {
		return this.guiaAmount;
	}

	public void setNumPack(String num) {
		this.numPack = num;
	}

	public String getNumPack() {
		return this.numPack;
	}

	public void setDestBranch(String descbr) {
		this.destBranch = descbr;

	}

	public String getDestBranch() {
		return this.destBranch;
	}

	// Added by Rajkumar for Branch Location Type
	public void setBranchLocType(String branchLocType) {
		this.branchLocType = branchLocType;

	}

	public String getBranchLocType() {
		return this.branchLocType;
	}

	public String getOrigBranch() {
		return origBranch;
	}

	public void setOrigBranch(String origBranch) {
		this.origBranch = origBranch;
	}

}
