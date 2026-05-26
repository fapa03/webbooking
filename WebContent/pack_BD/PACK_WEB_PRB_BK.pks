CREATE OR REPLACE PACKAGE SIPPRB."PACK_WEB" IS
  language_id VARCHAR2(3):='ESP';
  FUNCTION FUN_ENCR_DECR_PSWD(PWD VARCHAR2,PWD_TYPE VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_clnt_name(clnt_id VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_clnt_user_name(user_id VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_Emails(clnt_id VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_ccosto_guia(guia_no VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_ccosto_descr(cliente_id VARCHAR2, ccosto VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_rfc(CLNT_ID VARCHAR2) RETURN VARCHAR2;  
  FUNCTION Fun_ftch_desc_client_type(client_type VARCHAR2) RETURN VARCHAR2;
  FUNCTION Fun_ftch_desc_credit_status(credit_status VARCHAR2) RETURN VARCHAR2;
  FUNCTION FUN_DISP_ADDL_SRVC(CLNT_id VARCHAR2,TRIF_SLAB VARCHAR2,ORGN_BRNC VARCHAR2,DEST_BRNC VARCHAR2) RETURN VARCHAR2;
  FUNCTION FUN_FTCH_WT_VOL_AMNT(clnt_id VARCHAR2, orgn_brnc VARCHAR2,dest_brnc VARCHAR2,trif_slab VARCHAR2, refr_serv_id VARCHAR2, serv_id VARCHAR2,weight NUMBER,VOLUME NUMBER) RETURN NUMBER;


/*End of package specification*/
  FUNCTION fun_ftch_form_no(clnt_id VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_ftch_form_no_fe(brnc_id VARCHAR2) RETURN number;   
  FUNCTION fun_ftch_form_no_fe_new(brnc_id VARCHAR2, modulo VARCHAR2, serieCaja VARCHAR2) RETURN number;
  FUNCTION fun_ftch_time_diff(site_id VARCHAR2) RETURN NUMBER;
  FUNCTION fun_ftch_ead_rout(brnc_id VARCHAR2, col_code VARCHAR2) RETURN VARCHAR2;
  FUNCTION fun_chk_inv_req(client_id varchar2) RETURN VARCHAR2;
  FUNCTION FUN_SRVC_NAME(SRVC_ID VARCHAR2) RETURN VARCHAR2;
  FUNCTION FUN_CALC_DISC(p_service_id in varchar2,p_client_id in varchar2,p_tariff_amt in Number,p_Quantity number, min_amt number,disc_slab out varchar2) RETURN NUMBER;
  PROCEDURE pro_insr_guia (guia_no VARCHAR2, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2);
  FUNCTION FUN_DFLT_DLVY(CLIENT_ID VARCHAR2, SRVC VARCHAR2) RETURN VARCHAR2;
  PROCEDURE PRO_BRNC_COLY(city VARCHAR2,colonia VARCHAR2,branch_id VARCHAR2,
                          lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2);                          
  PROCEDURE PRO_BRNC_COLY_ZE(city VARCHAR2,colonia VARCHAR2,branch_id VARCHAR2,
                          lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2, lc_ol OUT VARCHAR2);
  PROCEDURE PRO_SITE_COLY(city VARCHAR2,colonia VARCHAR2,site_id VARCHAR2,
                          lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2);
  PROCEDURE PRO_SHIP_DEST_MSTR(orgn_BRNC VARCHAR2,dest_BRNC VARCHAR2,ship_seqn OUT NUMBER,
                               no_ship OUT NUMBER,cur_loc OUT VARCHAR2,
                               cur_dest OUT VARCHAR2,lc_rout OUT VARCHAR2);
  PROCEDURE PRO_CLNT_INSU_FLAG(CLNT_ID VARCHAR2,INS_FLAG OUT VARCHAR2,PLCY_NO OUT VARCHAR2,EXPY_DATE OUT DATE, SYS_DATE OUT DATE);
  PROCEDURE PRO_SRVC_INFO (ins_type VARCHAR2,ins_amnt NUMBER,clnt_id VARCHAR2,ins_info OUT VARCHAR2);
  PROCEDURE PRO_COD_TEXT (cod_srvc_amt NUMBER, cod_text OUT VARCHAR2);
  FUNCTION FUN_GEN_GUIA_NO(orgn_brnc_id VARCHAR2)RETURN VARCHAR2;
  FUNCTION FUN_GUIA_SEQ_EXISTS(branch_id VARCHAR2)RETURN VARCHAR2;
  FUNCTION FUN_BRDR_BRNC(site_id VARCHAR2) RETURN VARCHAR2;
  FUNCTION FUN_CHCK_RAD_FLAG(clnt_id varchar2) RETURN VARCHAR2;
  FUNCTION FUN_DFLT_RAD (CLIENT_ID VARCHAR2, SRVC VARCHAR2)RETURN VARCHAR2;
  PROCEDURE PRO_DEFA_CONT(orgn_addr_code IN VARCHAR2,orgn_clnt_id IN VARCHAR2,
                          cont_id OUT VARCHAR2,iden_type OUT VARCHAR2,
                          iden_no OUT VARCHAR2,cont_name OUT VARCHAR2);
  PROCEDURE PRO_INSRT_WEB_BOK_STUS (GUIA_NO VARCHAR2,SHIP_SEQN_NO NUMBER,NO_SHIP NUMBER,
                                    CUR_LOC VARCHAR2,CUR_DEST VARCHAR2,ORGN_CLNT_ID VARCHAR2);
  PROCEDURE PRO_INSRT_WEB_BOK_ADDR(guia_no VARCHAR2, clnt_id VARCHAR2, addr_code_orgn VARCHAR2,
      addr_code_dest VARCHAR2, addr_code_fisc VARCHAR2);
  /*PROCEDURE PRO_INSRT_WEB_BOK_ADDR(addr_code IN VARCHAR2,addr_type IN VARCHAR2,
                                   guia_no        IN VARCHAR2,orgn_clnt_id   IN VARCHAR2,
                                   addr_line1     IN VARCHAR2,addr_line2 IN VARCHAR2,addr_line3     IN VARCHAR2,
                                   addr_line4     IN VARCHAR2,addr_line5 IN VARCHAR2,addr_line6     IN VARCHAR2,
                                   addr_line7     IN VARCHAR2,
                                   str_name IN VARCHAR2, drnr IN VARCHAR2,ph_no IN VARCHAR2,
                                   fl_no IN VARCHAR2, suit_no IN VARCHAR2,zipcode IN VARCHAR2,
                                   addr_cod1 IN VARCHAR2,addr_cod2 IN VARCHAR2,addr_cod3 IN VARCHAR2,
                                   addr_cod4 IN VARCHAR2,addr_cod5 IN VARCHAR2,addr_cod6 IN VARCHAR2);  */
  PROCEDURE PRO_INSRT_WEB_BOK_SRVC_ITEM (GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,REFR_SRVC IN VARCHAR2,
                                         DESCR IN VARCHAR2,CONTENT IN VARCHAR2,
                                         QTY IN VARCHAR2,SRVC_CHRG IN NUMBER,
                                         STUS_FLAG IN VARCHAR2,
                                         CREATD_ON IN DATE,CREATD_BY IN VARCHAR2,
                                         MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                         SLAB IN VARCHAR2,
                                         WEIGHT IN NUMBER,VOLUME IN NUMBER,
                                         DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2);
PROCEDURE PRO_INSRT_WEB_BOK_SRVC_ITEM_NE(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,REFR_SRVC IN VARCHAR2,
                                         DESCR IN VARCHAR2,CONTENT IN VARCHAR2,
                                         QTY IN VARCHAR2,SRVC_CHRG IN NUMBER,
                                         STUS_FLAG IN VARCHAR2,
                                         CREATD_ON IN DATE,CREATD_BY IN VARCHAR2,
                                         MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                         SLAB IN VARCHAR2,
                                         WEIGHT IN NUMBER,VOLUME IN NUMBER,
                                         DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2);

 PROCEDURE PRO_INSRT_WEB_BOK_SRVC(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,DISC IN NUMBER,
                                  TAX IN NUMBER,TAX_RET IN NUMBER,AMOUNT IN NUMBER ,
                                  FLAG IN VARCHAR2,SRVC_TYPE IN VARCHAR2,
                                  DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2,
                                  CRETD_ON IN DATE,CRETD_BY IN VARCHAR2,
                                  MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                  STUS_FLAG IN VARCHAR2,DISC_SLAB IN VARCHAR2);
 PROCEDURE PRO_INSRT_WEB_BOK_SRVC_NEW(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,DISC IN NUMBER,
                                  TAX IN NUMBER,TAX_RET IN NUMBER,AMOUNT IN NUMBER ,
                                  FLAG IN VARCHAR2,SRVC_TYPE IN VARCHAR2,
                                  DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2,
                                  CRETD_ON IN DATE,CRETD_BY IN VARCHAR2,
                                  MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                  STUS_FLAG IN VARCHAR2,DISC_SLAB IN VARCHAR2);
  PROCEDURE PRO_INSRT_WEB_BOK_ITEM_REQM(orgn_clnt_id IN VARCHAR2,addl_srvc_id IN VARCHAR2,
                                        guia_no IN VARCHAR2);
  FUNCTION fun_slmn_id(client_id varchar2) RETURN varchar2;
  PROCEDURE PRO_SRVC_NAME(SRVC_ID VARCHAR2,SRVC VARCHAR2, SRVC_NAME OUT VARCHAR2);
  FUNCTION FUN_GET_DISC_CLNT (ORGN_CLNT VARCHAR2, DEST_CLNT VARCHAR2, SRVC_ID VARCHAR2)RETURN NUMBER;
  FUNCTION FUN_FTCH_ADD_SRVC(SRVC_ID VARCHAR2, SRVC VARCHAR2,ORGN_BRNC VARCHAR2,
                       DEST_BRNC VARCHAR2,BRNC_ZONE VARCHAR2,
                       qty number,fact_type varchar2, min_amt OUT NUMBER) RETURN number;
  FUNCTION FUN_FTCH_ADD_RAD_SRVC(SRVC_ID VARCHAR2, SRVC VARCHAR2,ORGN_BRNC VARCHAR2,
                       DEST_BRNC VARCHAR2, qty number,fact_type varchar2, min_amt OUT NUMBER) RETURN number;
  PROCEDURE pro_ftch_destbrnc_report(guia_refr_no VARCHAR2,tariff_tot OUT NUMBER,
                                                       ack_tot OUT NUMBER,rad_tot OUT NUMBER,
                                     ead_tot OUT NUMBER,ins_tot OUT NUMBER,cod_tot OUT NUMBER,
                                     addl_tot OUT NUMBER,other_addl_tot OUT NUMBER,
                                     dest_brnc_id VARCHAR2);
  FUNCTION fun_chk_actv_trif (orgn_brnc VARCHAR2, dest_brnc VARCHAR2) RETURN NUMBER;
  FUNCTION fun_chk_dstn_brnch (orgn_brnc VARCHAR2, dest_brnc VARCHAR2) RETURN NUMBER;
  FUNCTION FUN_CLNT_CRDT_AMT(ARG_CLNT_ID_MN VARCHAR2) RETURN NUMBER;
  FUNCTION FUN_CLNT_CRDT_AMT_ADN (ARG_CLNT_ID_MN VARCHAR2) RETURN NUMBER;
  FUNCTION FUN_CLNT_CRDT_AMT_ADN_GRUP (ARG_CLNT_ID_GRUP VARCHAR2) RETURN NUMBER;
  FUNCTION fun_chck_grup_clnt(CLNT_ID IN VARCHAR2) RETURN varchar2;
  PROCEDURE pro_ftch_srvs_covd(trif_type VARCHAR2, ead OUT VARCHAR2, ack OUT VARCHAR2,
        cod OUT VARCHAR2, inv OUT VARCHAR2);
  PROCEDURE PRO_DEL_MNFT(manifestNumber in varchar2,clientid in varchar2);
  PROCEDURE PRO_DEL_MNFT_LOG(manifestNumber in varchar2, clientid in varchar2, userid in varchar2);
  FUNCTION fun_ftch_trif_exist(clnt_id VARCHAR2, orgn_brnc VARCHAR2, dest_brnc VARCHAR2,
        trif_type VARCHAR2) RETURN VARCHAR2;
    FUNCTION fun_ftch_trif_amnt(clnt_id VARCHAR2, orgn_brnc VARCHAR2, dest_brnc VARCHAR2,
        trif_type VARCHAR2, refr_serv_id VARCHAR2, serv_id VARCHAR2) RETURN NUMBER;
  FUNCTION fun_ftch_trif_amnt_new(clnt_id VARCHAR2,dest_brnc VARCHAR2, serv_id VARCHAR2, tarifa VARCHAR2) RETURN NUMBER;        
  PROCEDURE pro_ftch_addr(am_defn_type varchar2, am_ref_no varchar2, am_gcode varchar2,
        am_glevl number, am_gtype number,
         u11 out varchar2,u12 out varchar2,u13 out varchar2,
       u14 out varchar2,u15 out varchar2,u16 out varchar2,u17 out varchar2,zipcode out varchar2,
       c11 out varchar2,c12 out varchar2,c13 out varchar2,c14 out varchar2,c15 out varchar2,c16 out varchar2);
  PROCEDURE pro_ftch_unst(ref_no varchar2,p1 out varchar2,p2 out varchar2,
        p3 out varchar2,p4 out varchar2,p5 out varchar2,p6 out varchar2,p7 out varchar2);
  PROCEDURE pro_ftch_unst_dir_alt(ref_no varchar2,p1 out varchar2,p2 out varchar2,
        p3 out varchar2,p4 out varchar2,p5 out varchar2,p6 out varchar2,p7 out varchar2, dir_alt out varchar2);        
    procedure PRO_GET_GEOENTY(lc_mpc in varchar2, levl1 out varchar2,levl2 out varchar2,
        levl3 out varchar2,levl4 out varchar2,levl5 out varchar2,levl6 out varchar2,levl7 out varchar2,
        levc1 out varchar2, levc2 out varchar2, levc3 out varchar2,levc4 out varchar2,levc5 out varchar2,
        levc6 out varchar2,zipcode out varchar2, lc_lvl in varchar2, lc_typ in varchar2); -- level and type parameters included on 13-03-2003 by Hari
  FUNCTION FUN_MNFT_STUS_DESC(MDUL_ID VARCHAR2,PARM_TYPE VARCHAR2,PARM_CODE1 VARCHAR2)
        RETURN VARCHAR2;
  FUNCTION fun_ftch_dely_type(guia_no VARCHAR2, guia_type VARCHAR2, actv_flag VARCHAR2)
      RETURN VARCHAR2;
  PROCEDURE PRO_SHOW_MESG(module_id VARCHAR2,lang_id VARCHAR2,mesg_id NUMBER,
        mesg_subid NUMBER,var1 VARCHAR2,var2 VARCHAR2,var3 VARCHAR2,mesg_type OUT VARCHAR2,
        mesg_text OUT VARCHAR2);
     PROCEDURE PRO_GET_MESG(module_id VARCHAR,lang_id VARCHAR,
        mesg_id NUMBER,mesg_subid NUMBER,lv_mesg_type IN OUT VARCHAR,lv_mesg IN OUT VARCHAR,
        ln_defa_but IN OUT NUMBER,lb_activ IN OUT VARCHAR,ld_till IN OUT DATE,var1 IN VARCHAR,
        var2 IN VARCHAR,var3 IN VARCHAR);
  FUNCTION Fun_ftch_brnc_name(Brnc_id In Varchar2) RETURN Varchar2;
  FUNCTION FUN_GET_GUIA_STUS(guia_no number) RETURN varchar2;
  FUNCTION Fun_ftch_guia_stus_desc(guia_stus In Varchar2) RETURN Varchar2;
  FUNCTION Fun_ftch_cod_vlue(guia_no In Varchar2) RETURN NUMBER;
  PROCEDURE pro_ftch_curr_guia_addr(type1 IN VARCHAR2, guia_no In Varchar2,
        lc_strt OUT VARCHAR2, lc_drnr OUT VARCHAR2, lc_lin4 OUT VARCHAR2, lc_lin6 OUT VARCHAR2,
        lc_pho1 OUT VARCHAR2);
    FUNCTION fun_ftch_ship_desc(ship_code VARCHAR2, srvc VARCHAR2, refr_srvc VARCHAR2)
  RETURN VARCHAR2;
    FUNCTION fun_ftch_dlvy_conc(guia_no VARCHAR2) RETURN VARCHAR2;
    FUNCTION fun_ftch_dlvy_conc_name(dlvy_conc VARCHAR2, dest_clnt_id VARCHAR2, dest_clnt_name
        VARCHAR2) RETURN VARCHAR2;
    FUNCTION fun_ftch_bok_conc_name(bok_conc_id VARCHAR2) RETURN VARCHAR2;
    FUNCTION FUN_FTCH_ADD_SRVC_DISC_guia(guia_no VARCHAR2, srvc_id VARCHAR2) RETURN NUMBER;
    FUNCTION fun_ftch_add_srvc_disc(guia_no VARCHAR2, srvc_id VARCHAR2) RETURN VARCHAR2;
    PROCEDURE pro_ftch_guia_for_canc (orgn_clnt_id VARCHAR2, orgn_brnc_id IN OUT VARCHAR2, form_no IN OUT VARCHAR2,
        guia_no IN OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT VARCHAR2, guia_amnt OUT NUMBER, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2,site_name OUT VARCHAR2);
    PROCEDURE pro_ftch_guia_for_canc_DE (orgn_clnt_id VARCHAR2, orgn_brnc_id IN OUT VARCHAR2, form_no IN OUT VARCHAR2,
        guia_no IN OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2, isse_date OUT VARCHAR2, guia_amnt OUT NUMBER, 
        mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2,site_name OUT VARCHAR2, docu_type OUT VARCHAR2,mesg_id OUT NUMBER);
    PROCEDURE pro_del_guia(guia_no VARCHAR2, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2);
/*  PROCEDURE pro_ftch_detl_for_form_no(orgn_brnc_id IN VARCHAR2, form_no IN VARCHAR2,
        guia_no OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT DATE, guia_amnt OUT NUMBER, actv_flag OUT VARCHAR2);
    PROCEDURE pro_ftch_detl_for_guia_no(guia_no IN VARCHAR2, orgn_brnc_id OUT VARCHAR2,
        form_no OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT DATE, guia_amnt OUT NUMBER, actv_flag OUT VARCHAR2);*/

    /* Function that fetches the tariff amt for shp srvc and addl srvc from BOK_GUIA_SRVC */

    FUNCTION FUN_CALC_SHP_TOTL(GUIA_NO VARCHAR2) RETURN NUMBER;

    FUNCTION FUN_CALC_ADD_TOTL(GUIA_NO VARCHAR2) RETURN NUMBER;

        FUNCTION FUN_CHK_BRDR(site_id VARCHAR2 , clnt_id VARCHAR2) RETURN NUMBER; --Added by B.Emerson on 19/10/2006
        FUNCTION FUN_CHK_BRDR_NEW(site_id VARCHAR2 , clnt_id VARCHAR2, crtd_by_user VARCHAR2) RETURN NUMBER; --AAP
        FUNCTION FUN_CHK_BRDR_NEW_CCOSTO(site_id VARCHAR2 , clnt_id VARCHAR2, crtd_by_user VARCHAR2) RETURN NUMBER; --AAP
    PROCEDURE Pro_Calc_Totl(guia_no in varchar2, shp_totl out number, ack_totl out number,
                              rad_totl out number, ead_totl out number, ins_totl out number,
                            cod_totl out number, addl_totl out number,
                            other_addl_totl out number);


    -- Procedures and functions from PACK_GUIA_PRINT  - START
    --VARIABLE DECLARATION





    PROCEDURE PRO_GET_INV_MESG(lv_mesg1 IN OUT VARCHAR,var1 IN VARCHAR,
    var2 IN VARCHAR,var3 IN VARCHAR);
    -- Procedures and functions from PACK_GUIA_PRINT  - END


        --Start of Procedure declaration to delete the manifests that were excluded --
    Procedure pro_mnft_del(manifestNumber in varchar2,clientid in varchar2);
        --End of Procedure declaration to delete the manifests that were excluded --
    PROCEDURE PRO_GET_ALL(levl_gr VARCHAR2,type_gr VARCHAR2, code_gr VARCHAR2, gdesc OUT VARCHAR2, gtype OUT VARCHAR2, glevl OUT VARCHAR2, gcode OUT VARCHAR2);
    PROCEDURE pro_get_city (entity_id in VARCHAR2, city out VARCHAR2, type4 out VARCHAR2, levl4 out VARCHAR2, code4 out VARCHAR2, ref_no OUT varchar2);
    PROCEDURE pro_ftch_addr_postal(am_defn_type VARCHAR2, am_ref_no VARCHAR2, am_gcode VARCHAR2,
     am_glevl NUMBER, am_gtype NUMBER,
     u11 OUT VARCHAR2,u12 OUT VARCHAR2,u13 OUT VARCHAR2,
        u14 OUT VARCHAR2,u15 OUT VARCHAR2,u16 OUT VARCHAR2,u17 OUT VARCHAR2,zipcode OUT VARCHAR2,
        c11 OUT VARCHAR2,c12 OUT VARCHAR2,c13 OUT VARCHAR2,c14 OUT VARCHAR2,c15 OUT VARCHAR2,c16 OUT VARCHAR2);
    PROCEDURE pro_ftch_unst_postal(ref_no VARCHAR2,P1 OUT VARCHAR2,p2 OUT VARCHAR2,
    p3 OUT VARCHAR2,p4 OUT VARCHAR2,p5 OUT VARCHAR2,p6 OUT VARCHAR2,p7 OUT VARCHAR2);
    Procedure pro_get_geoenty_postal(lc_mpc IN VARCHAR2, levl1 OUT VARCHAR2,levl2 OUT VARCHAR2,
    levl3 OUT VARCHAR2,levl4 OUT VARCHAR2,levl5 OUT VARCHAR2,levl6 OUT VARCHAR2,levl7 OUT VARCHAR2,
    levc1 OUT VARCHAR2, levc2 OUT VARCHAR2, levc3 OUT VARCHAR2,levc4 OUT VARCHAR2,levc5 OUT VARCHAR2,
    levc6 OUT VARCHAR2,zipcode OUT VARCHAR2, lc_lvl IN VARCHAR2, lc_typ IN VARCHAR2);

/*    PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,eadcheck out varchar2, lc_levl out VARCHAR2,
        lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2);
*/
/*    PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,eadcheck out varchar2, lc_levl out VARCHAR2,
        lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2,lc_custClntId OUT VARCHAR2,lc_email OUT VARCHAR2,clientPQId VARCHAR2,lc_clientId out VARCHAR2);*/
PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,
dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,
brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,
eadcheck out varchar2, lc_levl out VARCHAR2,
   lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,
   POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2,
   lc_email OUT VARCHAR2,lc_clientId out VARCHAR2);
----
--FOR DC EXTENDED FUNCTIONS AND PROCEDURES

FUNCTION Fun_ftch_site_name(Site_id In Varchar2) RETURN Varchar2;
PROCEDURE PRO_ASSIGN_ORIGIN_BRNC (GH_DEST_CLNT_ID VARCHAR2, OUT_DEST_BR OUT VARCHAR2 );
PROCEDURE PRO_ASSIGN_DEST_BRNC (LV_GE_CODE VARCHAR2,LV_GE_TYPE VARCHAR2, LV_GE_LEVEL VARCHAR2, OUT_DEST_BR OUT VARCHAR2,OUT_DEST_BR_NAME OUT VARCHAR2,DEST_SITE VARCHAR2);
FUNCTION fun_chk_no_of_br(GH_DEST_SITE_ID varchar2) RETURN NUMBER;
FUNCTION fun_chk_phy_dc_exist(GH_DEST_SITE_ID varchar2) RETURN NUMBER;
--FUNCTION FUN_DFLT_DLVY_NEW(CLIENT_ID VARCHAR2, SRVC VARCHAR2) RETURN boolean;
procedure pro_insrt_web_ctrl_email(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2,
     CE_DEST_CLNT_ID VARCHAR2, CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2, 
     out_file1 OUT VARCHAR2);
procedure pro_insrt_web_ctrl_email_right(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2,
     CE_DEST_CLNT_ID VARCHAR2, CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2, 
     out_file1 OUT VARCHAR2);     
procedure pro_insrt_web_ctrl_email_tmp(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2,
     CE_DEST_CLNT_ID VARCHAR2, CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2, 
     out_file1 OUT VARCHAR2);       
procedure pro_insrt_web_ctrl_email_tmpr(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2,
     CE_DEST_CLNT_ID VARCHAR2, CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2, 
     out_file1 OUT VARCHAR2);        
PROCEDURE pro_val_input_email(CLNT_ORGN_ID VARCHAR2, CLNT_DEST_ID VARCHAR2, EMAIL_ORGN OUT VARCHAR2, EMAIL_DEST OUT VARCHAR2);
PROCEDURE PRO_CARGA_CLNT_DEST(ORGN_BRNC_ID VARCHAR2, ORGN_CLNT_ID VARCHAR2, CLNT_FUENTE VARCHAR2, FECHA_INI VARCHAR2, FECHA_FIN VARCHAR2, USUARIO_SIPWEB VARCHAR2);
FUNCTION FUN_GET_TARIFF (WEIGHT number,VOLUME number) RETURN varchar2; 
PROCEDURE INSER_ORDER_STUS (lc_ct_trns_type VARCHAR2, l_ct_trns_no VARCHAR2, LV_ORDER_NO VARCHAR2, LV_CLINT_ORGN VARCHAR2, LV_MOTIV VARCHAR2, LV_ORDER_TYPE VARCHAR2, LV_CURR_LOCA VARCHAR2,user_id VARCHAR2 ) ;

END;
/