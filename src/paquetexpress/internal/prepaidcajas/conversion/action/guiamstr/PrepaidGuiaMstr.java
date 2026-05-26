package paquetexpress.internal.prepaidcajas.conversion.action.guiamstr;
import java.util.*;

public class PrepaidGuiaMstr {
	String bgm_ref_no="";
	 String bgm_prep_brnc_id="";
	 Date bgm_ref_dt;
	 String bgm_orgn_brnc_id="";
	 String bgm_clint_id="";
	 double bgm_no_of_guis;
	 String bgm_zone="";
	 double bgm_wght;
	 String bgm_vlum="";
	 double bgm_gh_amt;
	 String bgm_valid_flg="";
	 String bgm_org_site_id="";
	 String bgm_ship_type="";
	 Date crtd_on;
	 String crtd_by="";
	 Date mdfd_on;
	 String mdfd_by="";
	 String bgm_swingcd_no="";
	 String bgm_ack_service="";
	 double bgm_ack_amt;
	 double bgm_ack_tax;
	 String bgm_insur_service="";
	 double bgm_insur_amt;
	 double bgm_insur_tax;
	 double bgm_dsc_percent;
	 double bgm_dsc_amt;
	 String bgm_ead_service="";
	 double bgm_ead_amt;
	 String bgm_rad_service="";
	 double bgm_rad_amt;
	 private String bgm_ext_service="";
	 private double bgm_ext_amt;
	 String bgm_tarifa="";
	 private String locationType;//AAP20
	 private String companyId;
	 
	public void setBgm_ref_no( String bgm_ref_no) {
		this.bgm_ref_no = bgm_ref_no;
	}
	public String getBgm_ref_no() {
		return bgm_ref_no;
	}
	public void setBgm_prep_brnc_id( String bgm_prep_brnc_id) {
		this.bgm_prep_brnc_id = bgm_prep_brnc_id;
	}
	public String getBgm_prep_brnc_id() {
		return bgm_prep_brnc_id;
	}
	public void setBgm_ref_dt( Date bgm_ref_dt) {
		this.bgm_ref_dt = bgm_ref_dt;
	}
	public Date getBgm_ref_dt() {
		return bgm_ref_dt;
	}
	public void setBgm_orgn_brnc_id( String bgm_orgn_brnc_id) {
		this.bgm_orgn_brnc_id = bgm_orgn_brnc_id;
	}
	public String getBgm_orgn_brnc_id() {
		return bgm_orgn_brnc_id;
	}
	public void setBgm_clint_id( String bgm_clint_id) {
		this.bgm_clint_id = bgm_clint_id;
	}
	public String getBgm_clint_id() {
		return bgm_clint_id;
	}
	public void setBgm_no_of_guis( double bgm_no_of_guis) {
		this.bgm_no_of_guis = bgm_no_of_guis;
	}
	public double getBgm_no_of_guis() {
		return bgm_no_of_guis;
	}
	public void setBgm_zone( String bgm_zone) {
		this.bgm_zone = bgm_zone;
	}
	public String getBgm_zone() {
		return bgm_zone;
	}
	public void setBgm_wght( double bgm_wght) {
		this.bgm_wght = bgm_wght;
	}
	public double getBgm_wght() {
		return bgm_wght;
	}
	public void setBgm_vlum( String bgm_vlum) {
		this.bgm_vlum = bgm_vlum;
	}
	public String getBgm_vlum() {
		return bgm_vlum;
	}
	public void setBgm_gh_amt( double bgm_gh_amt) {
		this.bgm_gh_amt = bgm_gh_amt;
	}
	public double getBgm_gh_amt() {
		return bgm_gh_amt;
	}
	public void setBgm_valid_flg( String bgm_valid_flg) {
		this.bgm_valid_flg = bgm_valid_flg;
	}
	public String getBgm_valid_flg() {
		return bgm_valid_flg;
	}
	public void setBgm_org_site_id( String bgm_org_site_id) {
		this.bgm_org_site_id = bgm_org_site_id;
	}
	public String getBgm_org_site_id() {
		return bgm_org_site_id;
	}
	public void setBgm_ship_type( String bgm_ship_type) {
		this.bgm_ship_type = bgm_ship_type;
	}
	public String getBgm_ship_type() {
		return bgm_ship_type;
	}
	public void setCrtd_on( Date crtd_on) {
		this.crtd_on = crtd_on;
	}
	public Date getCrtd_on() {
		return crtd_on;
	}
	public void setCrtd_by( String crtd_by) {
		this.crtd_by = crtd_by;
	}
	public String getCrtd_by() {
		return crtd_by;
	}
	public void setMdfd_on( Date mdfd_on) {
		this.mdfd_on = mdfd_on;
	}
	public Date getMdfd_on() {
		return mdfd_on;
	}
	public void setMdfd_by( String mdfd_by) {
		this.mdfd_by = mdfd_by;
	}
	public String getMdfd_by() {
		return mdfd_by;
	}
	public void setBgm_swingcd_no( String bgm_swingcd_no) {
		this.bgm_swingcd_no = bgm_swingcd_no;
	}
	public String getBgm_swingcd_no() {
		return bgm_swingcd_no;
	}
	public void setBgm_ack_service( String bgm_ack_service) {
		this.bgm_ack_service = bgm_ack_service;
	}
	public String getBgm_ack_service() {
		return bgm_ack_service;
	}
	public void setBgm_ack_amt( double bgm_ack_amt) {
		this.bgm_ack_amt = bgm_ack_amt;
	}
	public double getBgm_ack_amt() {
		return bgm_ack_amt;
	}
	public void setBgm_ack_tax( double bgm_ack_tax) {
		this.bgm_ack_tax = bgm_ack_tax;
	}
	public double getBgm_ack_tax() {
		return bgm_ack_tax;
	}
	public void setBgm_insur_service( String bgm_insur_service) {
		this.bgm_insur_service = bgm_insur_service;
	}
	public String getBgm_insur_service() {
		return bgm_insur_service;
	}
	public void setBgm_insur_amt( double bgm_insur_amt) {
		this.bgm_insur_amt = bgm_insur_amt;
	}
	public double getBgm_insur_amt() {
		return bgm_insur_amt;
	}
	public void setBgm_insur_tax( double bgm_insur_tax) {
		this.bgm_insur_tax = bgm_insur_tax;
	}
	public double getBgm_insur_tax() {
		return bgm_insur_tax;
	}
	public void setBgm_dsc_percent( double bgm_dsc_percent) {
		this.bgm_dsc_percent = bgm_dsc_percent;
	}
	public double getBgm_dsc_percent() {
		return bgm_dsc_percent;
	}
	public void setBgm_dsc_amt( double bgm_dsc_amt) {
		this.bgm_dsc_amt = bgm_dsc_amt;
	}
	public double getBgm_dsc_amt() {
		return bgm_dsc_amt;
	}
	public void setBgm_ead_service( String bgm_ead_service) {
		this.bgm_ead_service = bgm_ead_service;
	}
	public String getBgm_ead_service() {
		return bgm_ead_service;
	}
	public void setBgm_ead_amt( double bgm_ead_amt) {
		this.bgm_ead_amt = bgm_ead_amt;
	}
	public double getBgm_ead_amt() {
		return bgm_ead_amt;
	}
	public void setBgm_rad_service( String bgm_rad_service) {
		this.bgm_rad_service = bgm_rad_service;
	}
	public String getBgm_rad_service() {
		return bgm_rad_service;
	}
	public void setBgm_rad_amt(double bgm_rad_amt) {
		this.bgm_rad_amt = bgm_rad_amt;
	}
	public double getBgm_rad_amt() {
		return bgm_rad_amt;
	}
	public void setBgm_ext_service(String bgm_ext_service) {
		this.bgm_ext_service = bgm_ext_service;
	}
	public String getBgm_ext_service() {
		return bgm_ext_service;
	}
	public void setBgm_ext_amt(double bgm_ext_amt) {
		this.bgm_ext_amt = bgm_ext_amt;
	}
	public double getBgm_ext_amt() {
		return bgm_ext_amt;
	}	
	
	public void setBgm_tarifa(String bgm_tarifa) {
		this.bgm_tarifa = bgm_tarifa;
	}
	public String getBgm_tarifa() {
		return bgm_tarifa;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
