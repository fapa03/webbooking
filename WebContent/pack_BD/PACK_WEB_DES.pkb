CREATE OR REPLACE PACKAGE BODY SIPDES."PACK_WEB" IS
-------------------
/*General */
    FUNCTION FUN_ENCR_DECR_PSWD(PWD VARCHAR2,PWD_TYPE VARCHAR2) RETURN VARCHAR2 IS
         ecr VARCHAR2(15);
         dcr VARCHAR2(15);
         len NUMBER(2);
    BEGIN
    len := LENGTH(pwd);
    IF PWD_TYPE IN ('E','e') THEN
           FOR i IN 1..len LOOP
             ecr := ecr||CHR(ASCII(SUBSTR(pwd,i,1))+len);
           END LOOP;
           RETURN ECR;
    ELSIF PWD_TYPE IN ('D','d') THEN
               FOR i IN 1..len LOOP
             dcr := dcr||CHR(ASCII(SUBSTR(pwd,i,1))-len);
           END LOOP;
           RETURN dcr;
    ELSE
           RETURN '';
    END IF;
    END;
------------------
    FUNCTION fun_ftch_clnt_name(clnt_id VARCHAR2) RETURN VARCHAR2 IS --AAP20140126
        CURSOR cur_ftch_clnt_name IS
            SELECT cm_clnt_name
                FROM SYS_CLNT_MSTR
                WHERE cm_clnt_id = clnt_id;
        lc_clnt_name SYS_CLNT_MSTR.cm_clnt_name%TYPE;
    BEGIN
        OPEN cur_ftch_clnt_name;
        FETCH cur_ftch_clnt_name INTO lc_clnt_name;
        CLOSE cur_ftch_clnt_name;
        RETURN(lc_clnt_name);
    END;
------------------
FUNCTION fun_ftch_clnt_user_name(user_id VARCHAR2) RETURN VARCHAR2 IS
        CURSOR cur_ftch_clnt_user_name IS
            SELECT CU_USER_NAME
                FROM SYS_CLNT_USER
                WHERE CU_USER_ID = user_id;
        lc_clnt_user_name SYS_CLNT_USER.CU_USER_NAME%TYPE;
    BEGIN
        OPEN cur_ftch_clnt_user_name;
        FETCH cur_ftch_clnt_user_name INTO lc_clnt_user_name;
        CLOSE cur_ftch_clnt_user_name;
        RETURN(nvl(lc_clnt_user_name,''));
    END;
FUNCTION fun_ftch_Emails(clnt_id VARCHAR2) RETURN VARCHAR2 IS
   CURSOR cur_ftch_Emails IS
   SELECT AM_MAIL_ID
   FROM SYS_ADDR_MSTR
   WHERE AM_ENTY_ID = clnt_id AND AM_ADDR_STYP = 'FISCAL';
   LC_AM_MAIL_ID SYS_ADDR_MSTR.AM_MAIL_ID%TYPE;
BEGIN
   OPEN cur_ftch_Emails;
   FETCH cur_ftch_Emails INTO LC_AM_MAIL_ID;
   CLOSE cur_ftch_Emails;
   RETURN(LC_AM_MAIL_ID);
END;
FUNCTION fun_ftch_ccosto_guia(guia_no VARCHAR2) RETURN VARCHAR2 IS
   CURSOR cur_ftch_Emails IS
   SELECT GE_CLNT_CCOSTO_ID
   FROM BOK_GUIA_HEAD_EXTRA
   WHERE GE_GUIA_NO = guia_no;
   LC_GE_GUIA_NO BOK_GUIA_HEAD_EXTRA.GE_GUIA_NO%TYPE;
BEGIN
   OPEN cur_ftch_Emails;
   FETCH cur_ftch_Emails INTO LC_GE_GUIA_NO;
   CLOSE cur_ftch_Emails;
   RETURN(LC_GE_GUIA_NO);
END;
FUNCTION fun_ftch_ccosto_descr(cliente_id VARCHAR2, ccosto VARCHAR2) RETURN VARCHAR2 IS
   CURSOR cur_ftch_ccosto IS
       SELECT CC_CCOSTO_NAME FROM SYS_CLNT_CCOSTO
           WHERE CC_CLNT_ID = cliente_id
           AND CC_CCOSTO_ID = ccosto;
   LC_ccosto_descr SYS_CLNT_CCOSTO.CC_CCOSTO_NAME%TYPE;
BEGIN
   IF ccosto IS NULL THEN
       LC_ccosto_descr :='';
   ELSE
       OPEN cur_ftch_ccosto;
       FETCH cur_ftch_ccosto INTO LC_ccosto_descr;
       CLOSE cur_ftch_ccosto;
   END IF;
   RETURN(LC_ccosto_descr);
END;
    FUNCTION fun_ftch_rfc(CLNT_ID VARCHAR2) RETURN VARCHAR2 IS
        lc_tax_id SYS_CLNT_MSTR.CM_TAX_ID%TYPE;
        CURSOR CUR_GET_TAX(LV_CLNT VARCHAR2) IS
            SELECT CM_TAX_ID
            FROM SYS_CLNT_MSTR
            WHERE CM_CLNT_ID = LV_CLNT;
    BEGIN
        OPEN CUR_GET_TAX(CLNT_ID);
        FETCH CUR_GET_TAX INTO lc_tax_id;
        IF CUR_GET_TAX%NOTFOUND THEN
            lc_tax_id := NULL;
        END IF;
        CLOSE CUR_GET_TAX;
        RETURN(lc_tax_id);
    END;
------------------
FUNCTION Fun_ftch_desc_client_type(client_type IN VARCHAR2) RETURN VARCHAR2 IS
        lv_client_type_desc SYS_PARM_MSTR.PM_VLUE1_DESC%TYPE;

      CURSOR cur_client_type(lv_mdul VARCHAR2,lv_parm_type VARCHAR2,lv_client_type VARCHAR2) IS
      SELECT pm_vlue1_desc
        FROM SYS_PARM_MSTR
          WHERE PM_MDUL_ID = lv_mdul
           AND PM_PARM_TYPE  = lv_parm_type
           AND PM_PARM_CODE1 = lv_client_type;
    BEGIN
        OPEN cur_client_type('SYS','CLIENT_TYPE',client_type);
        FETCH cur_client_type INTO lv_client_type_desc;
        IF cur_client_type%NOTFOUND THEN
            lv_client_type_desc := 'N/A';
        END IF;
        CLOSE cur_client_type;
        RETURN(lv_client_type_desc);
    END Fun_ftch_desc_client_type;
    ---------------------------------------
    FUNCTION Fun_ftch_desc_credit_status(credit_status IN VARCHAR2) RETURN VARCHAR2 IS
        lv_credit_status_desc SYS_PARM_MSTR.PM_VLUE1_DESC%TYPE;

      CURSOR cur_credit_status(lv_mdul VARCHAR2,lv_parm_type VARCHAR2,lv_credit_status VARCHAR2) IS
      SELECT pm_vlue1_desc
        FROM SYS_PARM_MSTR
          WHERE PM_MDUL_ID = lv_mdul
           AND PM_PARM_TYPE  = lv_parm_type
           AND PM_PARM_CODE1 = lv_credit_status;
    BEGIN
        OPEN cur_credit_status('SYS','CREDIT_STATUS',credit_status);
        FETCH cur_credit_status INTO lv_credit_status_desc;
        IF cur_credit_status%NOTFOUND THEN
            lv_credit_status_desc := 'N/A';
        END IF;
        CLOSE cur_credit_status;
        RETURN(lv_credit_status_desc);
    END Fun_ftch_desc_credit_status;

    FUNCTION FUN_DISP_ADDL_SRVC(CLNT_id VARCHAR2,TRIF_SLAB VARCHAR2,ORGN_BRNC VARCHAR2,DEST_BRNC VARCHAR2) RETURN
        VARCHAR2 IS
        lc_srvc_type WEB_CLNT_SRVC_TRIF.WT_SRVC_TYPE%TYPE;
        CURSOR CUR_DISP_ADDL_SRVC IS
            SELECT WT_SRVC_TYPE
            FROM   WEB_CLNT_SRVC_TRIF
            WHERE  WT_ORGN_CLNT_ID=clnt_id
            AND    WT_TRIF_SLAB=TRIF_SLAB
            AND    substr(WT_ORGN_BRNC_ID,1,3) = substr(ORGN_BRNC,1,3)
            AND    substr(WT_DEST_BRNC_ID,1,3) = substr(DEST_BRNC,1,3)
            AND    WT_SRVC_TYPE = 'A';
    BEGIN
        OPEN CUR_DISP_ADDL_SRVC;
        FETCH CUR_DISP_ADDL_SRVC INTO lc_srvc_type;
        CLOSE CUR_DISP_ADDL_SRVC;
        IF lc_srvc_type='A' THEN
            RETURN ('Y');
        END IF;
        RETURN ('N');
    END FUN_DISP_ADDL_SRVC;

    FUNCTION FUN_DISP_ADDL_SRVC_KM(CLNT_id VARCHAR2,TRIF_SLAB VARCHAR2,ORGN_BRNC VARCHAR2, km_Dist NUMBER) RETURN
        VARCHAR2 IS
        lc_srvc_type WEB_CLNT_SRVC_TRIF_KM.WT_SRVC_TYPE%TYPE;
        CURSOR CUR_DISP_ADDL_SRVC IS
            SELECT WT_SRVC_TYPE
            FROM   WEB_CLNT_SRVC_TRIF_KM
            WHERE  WT_ORGN_CLNT_ID=clnt_id
            AND    WT_TRIF_SLAB=TRIF_SLAB
            AND    km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM
            AND    WT_SRVC_TYPE = 'A';
    BEGIN
        OPEN CUR_DISP_ADDL_SRVC;
        FETCH CUR_DISP_ADDL_SRVC INTO lc_srvc_type;
        CLOSE CUR_DISP_ADDL_SRVC;
        IF lc_srvc_type='A' THEN
            RETURN ('Y');
        END IF;
        RETURN ('N');
    END FUN_DISP_ADDL_SRVC_KM;
------------------
 FUNCTION FUN_FTCH_WT_VOL_AMNT(clnt_id VARCHAR2, orgn_brnc VARCHAR2,dest_brnc VARCHAR2,trif_slab VARCHAR2,
    refr_serv_id VARCHAR2, serv_id VARCHAR2,weight NUMBER,VOLUME NUMBER)RETURN NUMBER IS
    lc_weight VARCHAR2(80);
    LC_VOLUME VARCHAR2(80);
    lc_fact_type VARCHAR2(80);
    lc_ftch_non WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    lc_ftch_kg WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    lc_ftch_cum WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    
    lc_ftch_kg_factor_value WEB_CLNT_SRVC_TRIF_FACTOR.WCP_FACTOR_VALUE%TYPE;--AAP07
    lc_ftch_kg_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;--AAP07
    lc_ftch_kg_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;--AAP07
    aditional_weight NUMBER;--AAP07
    
    lc_ftch_cum_factor_value WEB_CLNT_SRVC_TRIF_FACTOR.WCP_FACTOR_VALUE%TYPE;--AAP07
    lc_ftch_cum_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;--AAP07
    lc_ftch_cum_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;--AAP07
    aditional_volume NUMBER;--AAP07
    
    adiAmnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT_EXED%TYPE := 0;--ADAP26
    
    CURSOR CUR_FTCH_NON_WT_VOL(lc_fact_type VARCHAR2) IS
           SELECT WT_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF
            WHERE WT_ORGN_CLNT_ID = clnt_id         AND
                  substr(WT_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)    AND
                  substr(WT_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)    AND
                  WT_TRIF_SLAB    = trif_slab    AND
                  WT_REFR_SRVC_ID = refr_serv_id AND
                  WT_SRVC_ID      = serv_id      AND
                  WT_FACTOR          = lc_fact_type;
                  
     CURSOR CUR_FTCH_FACTOR_WT_VOL(lc_fact_type VARCHAR2) IS--AAP07
           SELECT WCP_TRIF_AMNT, WCP_FACTOR_VALUE, NVL(WCP_TRIF_AMNT_EXED,0)
             FROM WEB_CLNT_SRVC_TRIF_FACTOR
            WHERE WCP_ORGN_CLNT_ID = clnt_id         AND
                  substr(WCP_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)    AND
                  substr(WCP_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)    AND
                  WCP_TRIF_SLAB       = trif_slab    AND
                  WCP_REFR_SRVC_ID = refr_serv_id AND
                  WCP_SRVC_ID          = serv_id      AND
                  WCP_FACTOR           = lc_fact_type
            ORDER BY WCP_FACTOR_VALUE;
    BEGIN
        lc_ftch_kg_trif_amnt_acum :=0;--AAP07
        lc_ftch_cum_trif_amnt_acum :=0;--AAP07
        lc_weight:=weight;
        lc_volume:=volume;
        
        IF lc_weight=0 AND lc_volume=0 THEN

            OPEN CUR_FTCH_NON_WT_VOL('NON');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_non;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_non:=0;
              END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            RETURN (lc_ftch_non);

        ELSE
            OPEN CUR_FTCH_FACTOR_WT_VOL('KG');--AAP07
            LOOP--AAP07
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_kg_trif_amnt, lc_ftch_kg_factor_value, adiAmnt/*AAP26*/;--AAP07
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN--AAP07
                    EXIT;--AAP07
                END IF;--AAP07
                lc_ftch_kg := adiAmnt;--almacena costo kilo adicional--ADAP26
                lc_ftch_kg_trif_amnt_acum:=lc_ftch_kg_trif_amnt_acum + lc_ftch_kg_trif_amnt;--AAP07
                
                if lc_weight <= lc_ftch_kg_factor_value then--AAP07
                    begin--AAP07
                        aditional_weight := 0;--AAP07
                        exit;--AAP07
                    end;--AAP07
                 else--AAP07
                    begin--AAP07
                        aditional_weight := lc_weight - lc_ftch_kg_factor_value;--AAP07
                    end;--AAP07
                 end if;--AAP07                
             END LOOP;--AAP07
            CLOSE CUR_FTCH_FACTOR_WT_VOL;--AAP07 
            
            /*AAP26        
            open CUR_FTCH_NON_WT_VOL('KG');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_kg:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            */--AAP26
            IF lc_ftch_kg IS NULL THEN--AAP26 
                lc_ftch_kg :=0; --AAP26
            END IF; --AAP26
            
            IF lc_ftch_kg_trif_amnt_acum IS NULL THEN--AAP07 
                lc_ftch_kg_trif_amnt_acum :=0;--AAP07 
            END IF;--AAP07 
            
            IF lc_ftch_kg_trif_amnt_acum > 0 THEN--AAP07 
                IF aditional_weight >0 THEN--AAP07 
                    lc_ftch_kg_trif_amnt_acum := lc_ftch_kg_trif_amnt_acum + (aditional_weight*lc_ftch_kg);--AAP07 
                END IF;--AAP07 
            ELSE --AAP07 
                
                open CUR_FTCH_NON_WT_VOL('KG');
                FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

                IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
                    lc_ftch_kg:=0;
                END IF;
                CLOSE CUR_FTCH_NON_WT_VOL;
                
                lc_ftch_kg_trif_amnt_acum := lc_weight * lc_ftch_kg;--AAP07 
            END IF;--AAP07 
            
            adiAmnt :=0;--se reinicia valor de variable que mantiene costo adicional--AAP26
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('CUM');--AAP07
            LOOP--AAP07
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_cum_trif_amnt, lc_ftch_cum_factor_value, adiAmnt;--AAP07
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN--AAP07
                    EXIT;--AAP07
                END IF;--AAP07
                lc_ftch_cum := adiAmnt;--almacena costo volumen adicional --AAP26
                lc_ftch_cum_trif_amnt_acum:=lc_ftch_cum_trif_amnt_acum + lc_ftch_cum_trif_amnt;--AAP07
                
                if lc_volume <= lc_ftch_cum_factor_value then--AAP07
                    begin--AAP07
                        aditional_volume := 0;--AAP07
                        exit;--AAP07
                    end;--AAP07
                 else--AAP07
                    begin--AAP07
                        aditional_volume := lc_volume - lc_ftch_cum_factor_value;--AAP07
                    end;--AAP07
                 end if;--AAP07                
             END LOOP;--AAP07
            CLOSE CUR_FTCH_FACTOR_WT_VOL;--AAP07
            
            /*--AAP26
            open CUR_FTCH_NON_WT_VOL('CUM');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_cum:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            */--AAP26
            IF lc_ftch_cum IS NULL THEN--AAP26 
                lc_ftch_cum :=0; --AAP26
            END IF;--AAP26
            
             IF lc_ftch_cum_trif_amnt_acum IS NULL THEN--AAP07 
                lc_ftch_cum_trif_amnt_acum :=0;--AAP07 
            END IF;--AAP07 
            
            IF lc_ftch_cum_trif_amnt_acum > 0 THEN--AAP07 
                IF aditional_volume >0 THEN--AAP07 
                    lc_ftch_cum_trif_amnt_acum := lc_ftch_cum_trif_amnt_acum + (aditional_volume*lc_ftch_cum);--AAP07 
                END IF;--AAP07 
            ELSE --AAP07 
                open CUR_FTCH_NON_WT_VOL('CUM');
                FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

                IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
                   lc_ftch_cum:=0;
                END IF;
                CLOSE CUR_FTCH_NON_WT_VOL;
                
                lc_ftch_cum_trif_amnt_acum := lc_volume * lc_ftch_cum;--AAP07 
            END IF;--AAP07 

            --IF lc_weight*lc_ftch_kg > LC_VOLUME * lc_ftch_cum THEN--AAP07
               --RETURN (lc_weight*lc_ftch_kg);--AAP07
            --ELSE
               --RETURN (LC_VOLUME * lc_ftch_cum);
            --END IF;
            IF lc_ftch_kg_trif_amnt_acum > lc_ftch_cum_trif_amnt_acum THEN--AAP07
               RETURN (lc_ftch_kg_trif_amnt_acum);--AAP07
            ELSE--AAP07
               RETURN (lc_ftch_cum_trif_amnt_acum);--AAP07
            END IF;--AAP07
            
            /*HABILITAR ESTE CÓDIGO SI SE REQUIERE OBTENER EL COSTO DE 'NON' SI NO PUDO OBTENER IMPORTE POR PESO Y VOLUMEN*/
            /*IF lc_ftch_kg_trif_amnt_acum > 0 OR lc_ftch_cum_trif_amnt_acum > 0 THEN
                IF lc_ftch_kg_trif_amnt_acum > lc_ftch_cum_trif_amnt_acum THEN--AAP07
                   RETURN (lc_ftch_kg_trif_amnt_acum);--AAP07
                ELSE--AAP07
                   RETURN (lc_ftch_cum_trif_amnt_acum);--AAP07
                END IF;--AAP07
            ELSE    
                OPEN CUR_FTCH_NON_WT_VOL('NON');
                FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_non;

                IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
                   lc_ftch_non:=0;
                  END IF;
                CLOSE CUR_FTCH_NON_WT_VOL;
                RETURN (lc_ftch_non);
            END IF;*/
        END IF;
    END FUN_FTCH_WT_VOL_AMNT;

 FUNCTION FUN_FTCH_WT_VOL_AMNT_KM(clnt_id VARCHAR2, orgn_brnc VARCHAR2, km_dist NUMBER,trif_slab VARCHAR2,
    refr_serv_id VARCHAR2, serv_id VARCHAR2,weight NUMBER,VOLUME NUMBER)RETURN NUMBER IS
    lc_weight VARCHAR2(80);
    LC_VOLUME VARCHAR2(80);
    lc_fact_type VARCHAR2(80);
    lc_ftch_non WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    lc_ftch_kg WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    lc_ftch_cum WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    
    lc_ftch_kg_factor_value WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_FACTOR_VALUE%TYPE;--AAP07
    lc_ftch_kg_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;--AAP07
    lc_ftch_kg_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;--AAP07
    aditional_weight NUMBER;--AAP07
    
    lc_ftch_cum_factor_value WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_FACTOR_VALUE%TYPE;--AAP07
    lc_ftch_cum_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;--AAP07
    lc_ftch_cum_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;--AAP07
    aditional_volume NUMBER;--AAP07
    
    adiAmnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT_EXED%TYPE := 0;--AAP26  

    CURSOR CUR_FTCH_NON_WT_VOL(lc_fact_type VARCHAR2) IS
           SELECT WT_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF_KM
            WHERE WT_ORGN_CLNT_ID = clnt_id AND
                  km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND
                  WT_TRIF_SLAB    = trif_slab    AND
                  WT_REFR_SRVC_ID = refr_serv_id AND
                  WT_SRVC_ID      = serv_id      AND
                  WT_FACTOR          = lc_fact_type;
                  
      CURSOR CUR_FTCH_FACTOR_WT_VOL(lc_fact_type VARCHAR2) IS--AAP07
           SELECT WCK_TRIF_AMNT, WCK_FACTOR_VALUE, NVL(WCK_TRIF_AMNT_EXED,0)
             FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM
            WHERE WCK_ORGN_CLNT_ID = clnt_id         AND
                  km_dist between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM AND
                  WCK_TRIF_SLAB       = trif_slab    AND
                  WCK_REFR_SRVC_ID = refr_serv_id AND
                  WCK_SRVC_ID          = serv_id      AND
                  WCK_FACTOR           = lc_fact_type
            ORDER BY WCK_FACTOR_VALUE;
    BEGIN
        lc_ftch_kg_trif_amnt_acum :=0;--AAP07
        lc_ftch_cum_trif_amnt_acum :=0;--AAP07
        lc_weight:=weight;
        lc_volume:=volume;
        IF lc_weight=0 AND lc_volume=0 THEN

            OPEN CUR_FTCH_NON_WT_VOL('NON');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_non;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_non:=0;
              END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            RETURN (lc_ftch_non);

        ELSE
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('KG');--AAP07
            LOOP--AAP07
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_kg_trif_amnt, lc_ftch_kg_factor_value, adiAmnt ;--AAP07
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN--AAP07
                    EXIT;--AAP07
                END IF;--AAP07
                lc_ftch_kg := adiAmnt;--almacena costo kilo adicional --AAP26
                lc_ftch_kg_trif_amnt_acum:=lc_ftch_kg_trif_amnt_acum + lc_ftch_kg_trif_amnt;--AAP07
                
                if lc_weight <= lc_ftch_kg_factor_value then--AAP07
                    begin--AAP07
                        aditional_weight := 0;--AAP07
                        exit;--AAP07
                    end;--AAP07
                 else--AAP07
                    begin--AAP07
                        aditional_weight := lc_weight - lc_ftch_kg_factor_value;--AAP07
                    end;--AAP07
                 end if;--AAP07                
             END LOOP;--AAP07
            CLOSE CUR_FTCH_FACTOR_WT_VOL;--AAP07 
            
            /*--AAP26
            open CUR_FTCH_NON_WT_VOL('KG');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_kg:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            */--AAP26
            IF lc_ftch_kg IS NULL THEN--AAP26 
                lc_ftch_kg :=0; --AAP26
            END IF;--AAP26
            
            IF lc_ftch_kg_trif_amnt_acum IS NULL THEN--AAP07 
                lc_ftch_kg_trif_amnt_acum :=0;--AAP07 
            END IF;--AAP07 
            
            IF lc_ftch_kg_trif_amnt_acum > 0 THEN--AAP07 
                IF aditional_weight >0 THEN--AAP07 
                    lc_ftch_kg_trif_amnt_acum := lc_ftch_kg_trif_amnt_acum + (aditional_weight*lc_ftch_kg);--AAP07 
                END IF;--AAP07 
            ELSE --AAP07 
            
                open CUR_FTCH_NON_WT_VOL('KG');
                FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

                IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
                   lc_ftch_kg:=0;
                END IF;
                CLOSE CUR_FTCH_NON_WT_VOL;
                
                lc_ftch_kg_trif_amnt_acum := lc_weight * lc_ftch_kg;--AAP07 
            END IF;--AAP07 
            
            adiAmnt :=0;--se reinicia valor de variable que mantiene costo adicional --AAP26
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('CUM');--AAP07
            LOOP--AAP07
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_cum_trif_amnt, lc_ftch_cum_factor_value, adiAmnt;--AAP07
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN--AAP07
                    EXIT;--AAP07
                END IF;--AAP07
                lc_ftch_cum := adiAmnt;--almacena costo volumen adicional --AAP26
                lc_ftch_cum_trif_amnt_acum:=lc_ftch_cum_trif_amnt_acum + lc_ftch_cum_trif_amnt;--AAP07
                
                if lc_volume <= lc_ftch_cum_factor_value then--AAP07
                    begin--AAP07
                        aditional_volume := 0;--AAP07
                        exit;--AAP07
                    end;--AAP07
                 else--AAP07
                    begin--AAP07
                        aditional_volume := lc_volume - lc_ftch_cum_factor_value;--AAP07
                    end;--AAP07
                 end if;--AAP07                
             END LOOP;--AAP07
            CLOSE CUR_FTCH_FACTOR_WT_VOL;--AAP07

            /*--AAP26
            open CUR_FTCH_NON_WT_VOL('CUM');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_cum:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;
            */--AAP26
            IF lc_ftch_cum IS NULL THEN--AAP26 
                lc_ftch_cum :=0;--AAP26
            END IF;--AAP26
            
            IF lc_ftch_cum_trif_amnt_acum IS NULL THEN--AAP07 
                lc_ftch_cum_trif_amnt_acum :=0;--AAP07 
            END IF;--AAP07 
            
            IF lc_ftch_cum_trif_amnt_acum > 0 THEN--AAP07 
                IF aditional_volume >0 THEN--AAP07 
                    lc_ftch_cum_trif_amnt_acum := lc_ftch_cum_trif_amnt_acum + (aditional_volume*lc_ftch_cum);--AAP07 
                END IF;--AAP07 
            ELSE --AAP07 
            
                open CUR_FTCH_NON_WT_VOL('CUM');
                FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

                IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
                   lc_ftch_cum:=0;
                END IF;
                CLOSE CUR_FTCH_NON_WT_VOL;
                
                lc_ftch_cum_trif_amnt_acum := lc_volume * lc_ftch_cum;--AAP07 
            END IF;--AAP07 

            --IF lc_weight*lc_ftch_kg > LC_VOLUME * lc_ftch_cum THEN--AAP07
               --RETURN (lc_weight*lc_ftch_kg);--AAP07
            --ELSE
               --RETURN (LC_VOLUME * lc_ftch_cum);
            --END IF;
            IF lc_ftch_kg_trif_amnt_acum > lc_ftch_cum_trif_amnt_acum THEN--AAP07
               RETURN (lc_ftch_kg_trif_amnt_acum);--AAP07
            ELSE--AAP07
               RETURN (lc_ftch_cum_trif_amnt_acum);--AAP07
            END IF;--AAP07

        END IF;
    END FUN_FTCH_WT_VOL_AMNT_KM;
------------------
    FUNCTION fun_ftch_form_no(clnt_id VARCHAR2) RETURN VARCHAR2 IS
        CURSOR cur_ftch_form_no IS
            select WC_CNTR_CODE, WC_FORM_INTL, WC_FORM_FINL, WC_FORM_LAST
            from web_clnt_mstr where WC_CLNT_ID = clnt_id;
        lc_cntr_code VARCHAR2(2);
        ln_form_intl NUMBER;
        ln_form_finl NUMBER;
        ln_form_last NUMBER;
    BEGIN
        OPEN cur_ftch_form_no;
        FETCH cur_ftch_form_no INTO lc_cntr_code, ln_form_intl, ln_form_finl, ln_form_last;
        CLOSE cur_ftch_form_no;
        IF lc_cntr_code IS NULL OR ln_form_intl IS NULL OR ln_form_finl IS NULL OR (Nvl(ln_form_last,0)=ln_form_finl) THEN
            RETURN NULL;
        END IF;
        IF ln_form_last IS NULL THEN
            RETURN (lc_cntr_code||to_char(ln_form_intl));
        ELSE
            RETURN (lc_cntr_code||to_char(Nvl(ln_form_last,0)+1));
        END IF;
    END;
    FUNCTION fun_ftch_form_no_fe_new (brnc_id VARCHAR2, modulo VARCHAR2, serieCaja VARCHAR2)
      RETURN number
   IS
   -- COMMENTED AND UPDATE BELOW ON 25-10-2010 Purpose: To get from no. from sys_parm_mstr.
/*    CURSOR cur_ftch_form_no IS
      select WC_CNTR_CODE, WC_FORM_INTL, WC_FORM_FINL, WC_FORM_LAST
         from web_clnt_mstr where WC_CLNT_ID = brnc_id;
*/
      CURSOR cur_ftch_form_no
      IS
         SELECT     (pm_vlue1_id)+1
               FROM sys_parm_mstr
              WHERE pm_mdul_id = modulo
                AND pm_parm_type = 'FORM_NO'
                AND pm_parm_code1 = brnc_id
                AND pm_parm_code2 = serieCaja
         FOR UPDATE;
      ln_form_intl   NUMBER;
   BEGIN
      OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO ln_form_intl;
      CLOSE cur_ftch_form_no;
      RETURN ln_form_intl;
         /* OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO lc_cntr_code, ln_form_intl, ln_form_finl, ln_form_last;
      CLOSE cur_ftch_form_no;
      IF lc_cntr_code IS NULL OR ln_form_intl IS NULL OR ln_form_finl IS NULL OR (Nvl(ln_form_last,0)=ln_form_finl) THEN
         RETURN NULL;
      END IF;
      IF ln_form_last IS NULL THEN
         RETURN (lc_cntr_code||to_char(ln_form_intl));
      ELSE
         RETURN (lc_cntr_code||to_char(Nvl(ln_form_last,0)+1));
      END IF;
      */
   END;
------------------
   FUNCTION fun_ftch_form_no_fe (brnc_id VARCHAR2)
      RETURN number
   IS
   -- COMMENTED AND UPDATE BELOW ON 25-10-2010 Purpose: To get from no. from sys_parm_mstr.
/*    CURSOR cur_ftch_form_no IS
      select WC_CNTR_CODE, WC_FORM_INTL, WC_FORM_FINL, WC_FORM_LAST
         from web_clnt_mstr where WC_CLNT_ID = brnc_id;
*/
      CURSOR cur_ftch_form_no
      IS
         SELECT     (pm_vlue1_id)+1
               FROM sys_parm_mstr
              WHERE pm_mdul_id = 'WEB'
                AND pm_parm_type = 'FORM_NO'
                AND pm_parm_code1 = brnc_id
                AND pm_parm_code2 = 'WW'
         FOR UPDATE;
      ln_form_intl   NUMBER;
   BEGIN
      OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO ln_form_intl;
      CLOSE cur_ftch_form_no;
      RETURN ln_form_intl;
         /* OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO lc_cntr_code, ln_form_intl, ln_form_finl, ln_form_last;
      CLOSE cur_ftch_form_no;
      IF lc_cntr_code IS NULL OR ln_form_intl IS NULL OR ln_form_finl IS NULL OR (Nvl(ln_form_last,0)=ln_form_finl) THEN
         RETURN NULL;
      END IF;
      IF ln_form_last IS NULL THEN
         RETURN (lc_cntr_code||to_char(ln_form_intl));
      ELSE
         RETURN (lc_cntr_code||to_char(Nvl(ln_form_last,0)+1));
      END IF;
      */
   END;
------------------
    FUNCTION fun_ftch_time_diff(site_id VARCHAR2) RETURN NUMBER IS
        CURSOR cur_ftch_time_diff IS
            SELECT SM_TIME_DIF_FR_LM
                FROM sys_SITE_mstr
                WHERE SM_SITE_ID=site_id;
        ln_time_diff sys_SITE_mstr.SM_TIME_DIF_FR_LM%TYPE;
    BEGIN
        OPEN cur_ftch_time_diff;
        FETCH cur_ftch_time_diff INTO ln_time_diff;
        IF cur_ftch_time_diff%NOTFOUND THEN
            ln_time_diff:=0;
        END IF;
        close cur_ftch_time_diff;
        RETURN (ln_time_diff);
    END;
------------------
    FUNCTION fun_ftch_ead_rout(brnc_id VARCHAR2, col_code VARCHAR2) RETURN VARCHAR2 IS
        cursor cur_ead_rout is
            SELECT BC_EAD_ROUT_ID
            FROM ER_BRNC_COLY_MSTR
            WHERE BC_BRNC_ID = brnc_id
            AND BC_GETY_CODE_6 = col_code;
        lc_ead_rout bok_guia_head.gh_ead_defa_rout%TYPE;
    BEGIN
        open cur_ead_rout;
        fetch cur_ead_rout into lc_ead_rout;
        if cur_ead_rout%notfound then
            lc_ead_rout := NULL;
        end if;
        close cur_ead_rout;
        RETURN (lc_ead_rout);
    END;
------------------
    FUNCTION fun_chk_inv_req(client_id varchar2) RETURN varchar2 IS
        lc_flag varchar2(10);
        cursor cur_inv_req(lv_clnt_id varchar2) is select cm_invc_guia_flag from sys_clnt_mstr
        where cm_clnt_id=lv_clnt_id;
    BEGIN
        open cur_inv_req(client_id);
        fetch cur_inv_req into lc_flag;
        if cur_inv_req%notfound then
            lc_flag := '';
        end if;
        close cur_inv_req;

      if lc_flag = 'Y' then
          return('Y');
      else
          return('N');
      end if;
      Exception
          When no_data_found then
              return('Y');
    END;
------------------
    FUNCTION FUN_SRVC_NAME(SRVC_ID VARCHAR2) RETURN VARCHAR2 IS
        CURSOR CUR_SRVC_NAME IS
            SELECT SM_SRVC_NAME FROM SYS_SRVC_MSTR
            WHERE SM_SRVC_ID = SRVC_ID;
        LC_SRVC_NAME SYS_SRVC_MSTR.SM_SRVC_NAME%TYPE;
    BEGIN
      OPEN CUR_SRVC_NAME;
      FETCH CUR_SRVC_NAME INTO LC_SRVC_NAME;
      IF CUR_SRVC_NAME%NOTFOUND THEN
          LC_SRVC_NAME := NULL;
      END IF;
      CLOSE CUR_SRVC_NAME;
      RETURN(LC_SRVC_NAME);
    END;
------------------
/*calculate discount in parameter are service_id, client_id, total tariff amount of service
and summation of quantity of the service */
FUNCTION fun_calc_disc(p_service_id IN VARCHAR2
                                      ,p_client_id IN VARCHAR2
                                      ,p_tariff_amt IN NUMBER
                                      ,p_Quantity NUMBER, min_amt NUMBER,disc_slab OUT VARCHAR2)
RETURN NUMBER IS

lc_Discount_Flag                             CHAR(1);
lv_client_service_category         VARCHAR2(15);
lc_amount_or_percentage_flag        CHAR(1);
ln_minimum_amt                                    NUMBER(15,2);
ln_maximum_amt                                    NUMBER(15,2);
ln_value                                             NUMBER(15,2);
lc_Discount_Overide_Flag          CHAR(1);
ln_Discount                                            NUMBER(15,2);
ln_Discount_c             NUMBER(12,2);
ln_Discount_q             NUMBER(12,2);
ln_msgnum                 NUMBER(15,2);
lc_disc_catg              VARCHAR2(30);
LC_DISC_SLAB              VARCHAR2(30);

BEGIN

    LN_DISCOUNT := 0;
    BEGIN
  SELECT SM_DISC_FLAG
        ,SM_DISC_OVRD_FLAG
    INTO lc_Discount_Flag
        ,lc_Discount_Overide_Flag
    FROM SYS_SRVC_MSTR
     WHERE SM_SRVC_ID = p_service_id;

     EXCEPTION
        WHEN NO_DATA_FOUND THEN
            NULL;
     END;

 IF lc_Discount_Flag = 'C' THEN /* --client discount-- */
        BEGIN
         SELECT CS_CLNT_SRVC_CATY
            INTO lv_client_service_category
            FROM SYS_CLNT_SRVC
            WHERE CS_CLNT_ID = p_client_id
        AND CS_SRVC_ID = p_service_id;
         --:CTRL.DISC_SRVC_FLAG:= '1';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
            --:CTRL.DISC_SRVC_FLAG:= '0';
            lv_client_service_category  := NULL;
        END;

        BEGIN
          SELECT DC_AMNT_R_PERC_FLAG
             , DC_MIN_AMNT
             , DC_MAX_AMNT
             , DC_VLUE
          INTO lc_amount_or_percentage_flag
             , ln_minimum_amt
             , ln_maximum_amt
             , ln_value
          FROM ACT_DISC_C
          WHERE DC_SRVC_ID = p_service_id
          AND DC_CLNT_SRVC_CATG = lv_client_service_category;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                lc_amount_or_percentage_flag := NULL;
            ln_minimum_amt               := NULL;
            ln_maximum_amt               := NULL;
            ln_value                     := NULL;
        END;

        IF lc_amount_or_percentage_flag = 'P' THEN  /* --Percentage-- */

             ln_Discount := (NVL(ln_value,0)*NVL(p_tariff_amt,0))/100;
              IF  NVL(ln_maximum_amt,0)<>0 AND NVL(ln_Discount,0) > ln_maximum_amt THEN
                 ln_Discount:=ln_maximum_amt;
             ELSIF NVL(ln_minimum_amt,0)<>0 AND NVL(ln_Discount,0) < ln_minimum_amt THEN
                ln_Discount:=ln_Minimum_amt;
             ELSIF  (NVL(ln_Discount,0) > NVL(ln_minimum_amt,0)) AND (NVL(ln_Discount,0)< NVL(ln_maximum_amt,0)) THEN
         ln_Discount := ln_Discount;
             END IF;
             IF p_service_id='EAD' THEN--added by B.Emerson on 18/07/2002
               IF ROUND(NVL(min_amt,0),2)>ROUND(NVL(p_tariff_amt,0),2)-ROUND(NVL(ln_Discount,0),2) THEN
                    --:CTRL_ADD_SRVC.SUBTOTAL:=Round(Nvl(:parameter.p_min_amt,0),2);
                       ln_Discount :=0;
               END IF;
             END IF;

             /* Commented by Nirmala on 18/11/2002 To change the RAD calculation
               To allow discount even the RAD amount less than the minimum amount.
             if p_service_id='RAD' then--added by B.Emerson on 25/10/2002
                if Round(Nvl(:parameter.p_min_amt,0),2)>Round(Nvl(p_tariff_amt,0),2)-Round(Nvl(ln_Discount,0),2) then
                :CTRL_ADD_SRVC.SUBTOTAL:=Round(Nvl(:parameter.p_min_amt,0),2);
              ln_Discount :=0;
           end if;
       end if;*/
        ELSIF
           lc_amount_or_percentage_flag = 'A' THEN /* --Amount-- */
             ln_Discount := NVL(ln_value,0);
        END IF;

ELSIF lc_Discount_Flag = 'Q' THEN  /* --Quantity-- */
    IF p_service_id != 'ENVELOPES' THEN
      BEGIN
        SELECT DQ_AMNT_R_PERC_FLAG, DQ_MIN_AMNT, DQ_MAX_AMNT, DQ_VLUE,DQ_SLAB_NO
        INTO lc_amount_or_percentage_flag, ln_minimum_amt, ln_maximum_amt,ln_value,LC_DISC_SLAB
              FROM ACT_DISC_Q
              WHERE DQ_SRVC_ID = p_service_id
              AND   p_quantity BETWEEN DQ_FROM_QUNT  AND DQ_TO_QUNT;      -- add for quantity - find slab

       IF p_service_id = 'ENVELOPES' THEN
             disc_slab := LC_DISC_SLAB;
           NULL;
       ELSIF     p_service_id = 'PACKETS' THEN
             disc_slab := LC_DISC_SLAB;
           NULL;
       END IF;


      EXCEPTION
          WHEN NO_DATA_FOUND THEN
          lc_amount_or_percentage_flag := NULL;
          ln_minimum_amt               := NULL;
          ln_maximum_amt               := NULL;
          ln_value                     := NULL;
      END;

          IF  lc_amount_or_percentage_flag = 'P' THEN /* --Percentage-- */

            ln_Discount := (NVL(ln_value,0)*NVL(p_tariff_amt,0))/100;
               IF NVL(ln_maximum_amt,0)<>0 AND NVL(ln_Discount,0) > ln_maximum_amt THEN
                   ln_Discount:=ln_maximum_amt;
                 ELSIF NVL(ln_minimum_amt,0)<>0 AND NVL(ln_Discount,0) < ln_minimum_amt THEN
                        ln_Discount:=ln_Minimum_amt;
                 ELSIF (NVL(ln_Discount,0) > NVL(ln_minimum_amt,0)) AND (NVL(ln_Discount,0) < NVL(ln_maximum_amt,0)) THEN
                     ln_Discount := ln_Discount;
                 END IF;
                 IF lc_Discount_Flag != 'B' AND (LN_VALUE IS NOT NULL OR LN_VALUE > 0 )THEN
                     /*Check this*/
                     /*ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                     end if;*/
                     NULL;
                 END IF;

           ELSIF
                lc_amount_or_percentage_flag = 'A' THEN   /* --Amount-- */
                ln_Discount := NVL(ln_value,0);

                IF lc_Discount_Flag != 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN

                    /*Check this*/
                    /*ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900151,1,ln_value,NULL,NULL);
                        if ln_msgnum != alert_button1 then
                              ln_Discount := 0;
                        end if;*/
                        NULL;
                END IF;

         END IF;
          ELSE
              ln_discount := 0;
          END IF;
ELSIF lc_Discount_Flag = 'B' THEN  /* --Both-- */

 BEGIN
        BEGIN
         SELECT CS_CLNT_SRVC_CATY INTO  lv_client_service_category
         FROM SYS_CLNT_SRVC WHERE CS_CLNT_ID = p_client_id
         AND CS_SRVC_ID     = p_service_id;
              IF lc_Discount_Overide_Flag = 'C' THEN
                  lc_Discount_Flag := 'C'; --ADDED BY NIRMALA ON 20-09-2001
                     --:CTRL.DISC_SRVC_FLAG:= '1';
             END IF;
         EXCEPTION
             WHEN NO_DATA_FOUND THEN
             lv_client_service_category := NULL;
        END;

         BEGIN
          SELECT DC_AMNT_R_PERC_FLAG
             , DC_MIN_AMNT
             , DC_MAX_AMNT
             , DC_VLUE
          INTO lc_amount_or_percentage_flag
             , ln_minimum_amt
             , ln_maximum_amt
             , ln_value
          FROM ACT_DISC_C
          WHERE DC_SRVC_ID = p_service_id
          AND DC_CLNT_SRVC_CATG = lv_client_service_category;
         EXCEPTION
            WHEN NO_DATA_FOUND THEN
            lc_amount_or_percentage_flag := NULL;
          ln_minimum_amt               := NULL;
          ln_maximum_amt               := NULL;
          ln_value                     := NULL;
         END;

        IF lc_amount_or_percentage_flag = 'P' THEN /* --Percentage-- */

             ln_Discount_c := (NVL(ln_value,0)*NVL(p_tariff_amt,0))/100;

             IF NVL(ln_maximum_amt,0)<>0 AND NVL(ln_Discount_c,0) > ln_maximum_amt THEN
                ln_Discount_c:=ln_maximum_amt;
             ELSIF NVL(ln_minimum_amt,0)<>0 AND NVL(ln_Discount_c,0) < ln_minimum_amt THEN
                ln_Discount_c:=ln_Minimum_amt;
             ELSIF     (ln_Discount_c > NVL(ln_minimum_amt,0)) AND (ln_Discount_c < NVL(ln_maximum_amt,0)) THEN
                 ln_Discount_c := ln_discount_c;
             END IF;

        ELSIF lc_amount_or_percentage_flag = 'A' THEN    /* --Amount-- */
           ln_Discount_c := NVL(ln_value,0);
        END IF;
 EXCEPTION
            WHEN NO_DATA_FOUND THEN
                ln_Discount_c:=0;
 END;

     IF p_service_id != 'ENVELOPES' AND (ln_Discount_c IS NULL OR ln_Discount_c =0) THEN
        BEGIN
            BEGIN
           SELECT DQ_AMNT_R_PERC_FLAG, DQ_MIN_AMNT, DQ_MAX_AMNT, DQ_VLUE,DQ_SLAB_NO
       INTO lc_amount_or_percentage_flag, ln_minimum_amt, ln_maximum_amt,ln_value,LC_DISC_SLAB
             FROM ACT_DISC_Q
             WHERE DQ_SRVC_ID = p_service_id
             AND p_quantity BETWEEN DQ_FROM_QUNT  AND DQ_TO_QUNT;
             LC_DISCOUNT_FLAG := 'Q';

          EXCEPTION
              WHEN NO_DATA_FOUND THEN
              lc_amount_or_percentage_flag := NULL;
              ln_minimum_amt               := NULL;
              ln_maximum_amt               := NULL;
              ln_value                     := NULL;
          END;

          IF p_service_id = 'ENVELOPES' THEN
         disc_slab := LC_DISC_SLAB;
           NULL;
       ELSIF     p_service_id = 'PACKETS' THEN
       disc_slab := LC_DISC_SLAB;
           NULL;
       END IF;

        IF lc_amount_or_percentage_flag = 'P' THEN /* --Percentage-- */

             ln_Discount_q := (NVL(ln_value,0)*NVL(p_tariff_amt,0))/100;
              IF NVL(ln_Discount_q,0) > ln_maximum_amt THEN
                 ln_Discount_q:=ln_maximum_amt;
              ELSIF  NVL(ln_Discount_q,0) < ln_minimum_amt THEN
                     ln_Discount_q:=ln_Minimum_amt;
                ELSIF NVL(ln_Discount_q,0) > NVL(ln_minimum_amt,0) AND     NVL(ln_Discount_q,0) > NVL(ln_maximum_amt,0) THEN
                  ln_Discount_q := ln_discount;
                END IF;
        ELSIF lc_amount_or_percentage_flag = 'A' THEN   /* --Amount-- */
             ln_Discount_q := NVL(ln_value,0);
        END IF;

        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                ln_Discount_q:=0;
        END;


        IF lc_Discount_Overide_Flag = 'H' THEN
            IF NVL(ln_Discount_c,0) >= NVL(ln_Discount_q,0)THEN
                ln_Discount := ln_Discount_c;
            ELSE
                ln_Discount := ln_Discount_q;

                IF lc_Discount_Flag = 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN
                    /*Check this*/
                    /*ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                    end if;*/
                    NULL;
                END IF;

            END IF;
        ELSIF lc_Discount_Overide_Flag = 'L' THEN
            IF NVL(ln_Discount_c,0) <= NVL(ln_Discount_q,0) THEN
                 ln_Discount := ln_Discount_c;
            ELSE
                ln_Discount := ln_Discount_q;

                IF lc_Discount_Flag = 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN
                    /* Check this
                    ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                    end if;*/
                    NULL;
                END IF;

            END IF;
        ELSIF lc_Discount_Overide_Flag = 'Q' THEN
            ln_Discount := ln_Discount_q;
         IF lc_Discount_Flag = 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN
                    /*Check this
                    ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                     end if;*/
                     NULL;
        END IF;

    ELSIF lc_Discount_Overide_Flag = 'C' THEN
            ln_Discount := ln_Discount_c;
            --**********************-------
            IF ln_discount IS NULL OR ln_discount = 0 THEN
            IF p_service_id != 'ENVELOPES' THEN
      BEGIN
        SELECT DQ_AMNT_R_PERC_FLAG, DQ_MIN_AMNT, DQ_MAX_AMNT, DQ_VLUE,DQ_SLAB_NO
        INTO lc_amount_or_percentage_flag, ln_minimum_amt, ln_maximum_amt,ln_value,LC_DISC_SLAB
              FROM ACT_DISC_Q
              WHERE DQ_SRVC_ID = p_service_id
              AND   p_quantity BETWEEN DQ_FROM_QUNT  AND DQ_TO_QUNT;      -- add for quantity - find slab

       IF p_service_id = 'ENVELOPES' THEN
           disc_slab := LC_DISC_SLAB;
           NULL;
       ELSIF     p_service_id = 'PACKETS' THEN
       disc_slab := LC_DISC_SLAB;
       NULL;
       END IF;


      EXCEPTION
          WHEN NO_DATA_FOUND THEN
          lc_amount_or_percentage_flag := NULL;
          ln_minimum_amt               := NULL;
          ln_maximum_amt               := NULL;
          ln_value                     := NULL;
      END;

          IF  lc_amount_or_percentage_flag = 'P' THEN /* --Percentage-- */

            ln_Discount := (NVL(ln_value,0)*NVL(p_tariff_amt,0))/100;
               IF NVL(ln_maximum_amt,0)<>0 AND NVL(ln_Discount,0) > ln_maximum_amt THEN
                   ln_Discount:=ln_maximum_amt;
                 ELSIF NVL(ln_minimum_amt,0)<>0 AND NVL(ln_Discount,0) < ln_minimum_amt THEN
                        ln_Discount:=ln_Minimum_amt;
                 ELSIF (NVL(ln_Discount,0) > NVL(ln_minimum_amt,0)) AND (NVL(ln_Discount,0) < NVL(ln_maximum_amt,0)) THEN
                     ln_Discount := ln_Discount;
                 END IF;
                 IF lc_Discount_Flag != 'B' AND (LN_VALUE IS NOT NULL OR LN_VALUE > 0 )THEN
                     /*Check this
                     ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                     end if;*/
                     NULL;

                 END IF;

           ELSIF
                lc_amount_or_percentage_flag = 'A' THEN   /* --Amount-- */
                ln_Discount := NVL(ln_value,0);

                IF lc_Discount_Flag != 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN
                    /*Check this
                    ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900151,1,ln_value,NULL,NULL);
                        if ln_msgnum != alert_button1 then
                              ln_Discount := 0;
                        end if;*/
                        NULL;
                END IF;

           END IF;
           IF lc_Discount_Flag = 'B' AND(LN_VALUE IS NOT NULL OR LN_VALUE > 0 ) THEN
                    /*Check this
                    ln_msgnum := FUN_SHOW_MESG('BOK',:GLOBAL.LANGUAGE_ID,900144,1,ln_value,NULL,NULL);
                    if ln_msgnum != alert_button1 then
                          ln_Discount := 0;
                     end if;*/
                     NULL;
        END IF;
          ELSE
              ln_discount := 0;
          END IF;
      END IF;

      END IF;
        ELSE
            ln_discount := ln_Discount_c;
        END IF;
    END IF;

    ---FOR RECORD GROUP
    IF LC_DISCOUNT_FLAG = 'C' THEN
    --    lc_disc_catg := lv_client_service_category; COMMENTED BY NIRMALA ON 20/09/2001
         lc_disc_catg := 'CLIENT'; -- MODIFIED BY NIRMALA ON 20/09/2001
    ELSIF LC_DISCOUNT_FLAG = 'Q' THEN
        lc_disc_catg := 'QUANTITY';
    END IF;

    IF ln_Discount IS NULL OR ln_Discount =0 THEN
        lc_disc_catg := 'NONE';
        lc_amount_or_percentage_flag := NULL;
        ln_value     := NULL;
  END IF;

    --VALDT_RECORD_DISC(p_service_id,lc_disc_catg,lc_amount_or_percentage_flag,nvl(ln_value,0));
    RETURN(ROUND(ln_Discount,2));
END;
--------------------------
    PROCEDURE pro_insr_guia (guia_no VARCHAR2, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2) IS
    BEGIN
        NULL;
    END;

------------------------------------------------------------------------
    /* For Web Booking */
    /* This procedure will delete Manifest Details from Table WEB_MNFT_DETL for the given Manifest Number */
    /* Created By Vishnu Radhakrishnan on 18/04/2003 */
    PROCEDURE PRO_DEL_MNFT(manifestNumber IN VARCHAR2,clientid IN VARCHAR2)IS
        lc_transNo VARCHAR2(15);
        lc_maxSerlNo NUMBER;
        lc_rfm VARCHAR2(3):='RFM';--added by B.Emerson on 07/06/2003
        lc_iim VARCHAR2(3):='IIM';--added by B.Emerson on 07/06/2003
        lc_null bok_guia_head.gh_guia_refr_no%TYPE:=NULL;--added by B.Emerson on 07/06/2003
        lc_h VARCHAR2(1)  :='H';--added by B.Emerson on 07/06/2003
        lc_a VARCHAR2(1)  :='A';--added by B.Emerson on 07/06/2003
        ld_date DATE := SYSDATE;--added by B.Emerson on 07/06/2003
    CURSOR cur_ftch_guia(LC_manifestNumber VARCHAR2) IS
           SELECT gh_guia_no,GH_CURR_LOCA,GH_CURR_DEST
             FROM BOK_GUIA_HEAD
               WHERE gh_guia_type = lc_h
                AND gh_actv_flag = lc_a
            AND gh_guia_refr_no = lc_manifestNumber;
    BEGIN
         SELECT NVL(CT_TRNS_NO,'9999') INTO lc_transNo FROM COM_CTT
          WHERE CT_SECO_ENTY= manifestNumber
            AND CT_TRNS_TYPE = lc_iim
            AND CT_PRIM_ENTY = clientid;

         UPDATE COM_CTT SET CT_DATE_03 = SYSDATE
          WHERE CT_SECO_ENTY= manifestNumber
            AND CT_TRNS_TYPE = lc_iim
            AND CT_PRIM_ENTY = clientid;

        FOR i IN cur_ftch_guia(manifestNumber) LOOP
            SELECT NVL(MAX(GS_SERL_NO),0)+ 1
                  INTO lc_maxSerlNo
                  FROM BOK_GUIA_STUS
                  WHERE GS_GUIA_NO=i.gh_guia_no;--added by B.Emerson on 07/06/2003
            INSERT INTO BOK_GUIA_STUS
                    (GS_GUIA_NO,GS_STUS_CODE,GS_SERL_NO,GS_COM_TRNS_REF_CODE,
                     CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,GS_CTT_TRNS_TYPE,
                     GS_CURR_LOCA,GS_CURR_DEST)
                   VALUES(i.gh_guia_no,lc_rfm,lc_maxSerlNo,lc_transNo,
                               ld_date,clientid,ld_date,clientid,lc_rfm,
                          I.GH_CURR_LOCA,i.GH_CURR_DEST);

            /*added by B.Emerson on 07/06/2003*/
            UPDATE bok_guia_head SET gh_guia_stus = lc_rfm, gh_guia_refr_no = lc_null
                WHERE gh_guia_no = i.gh_guia_no AND
                gh_guia_type = lc_h AND
                gh_actv_flag = lc_a;
        END LOOP;
        DELETE FROM WEB_MNFT_DETL WHERE MD_MNFT_NO = manifestNumber;
        /*YA NO SE UTILIZA LA TABLA WEB_CTRL_EMAIL A NIVEL DE MANIFIESTO, AHORA ES POR GUIA*/
        --DELETE FROM WEB_CTRL_EMAIL WHERE CE_MNFT_NO = manifestNumber;--AAP02
        COMMIT;
    END;

------------------------------------------------------------------------
PROCEDURE PRO_DEL_MNFT_LOG(manifestNumber IN VARCHAR2, clientid IN VARCHAR2, userid IN VARCHAR2)IS
        lc_transNo VARCHAR2(15);
        lc_maxSerlNo NUMBER;
        lc_rfm VARCHAR2(3):='RFM';--added by B.Emerson on 07/06/2003
        lc_iim VARCHAR2(3):='IIM';--added by B.Emerson on 07/06/2003
        lc_null bok_guia_head.gh_guia_refr_no%TYPE:=NULL;--added by B.Emerson on 07/06/2003
        lc_h VARCHAR2(1)  :='H';--added by B.Emerson on 07/06/2003
        lc_a VARCHAR2(1)  :='A';--added by B.Emerson on 07/06/2003
        ld_date DATE := SYSDATE;--added by B.Emerson on 07/06/2003
        lc_amno_code VARCHAR2(2):='88';
        LV_RECD_SEQN_NO  COM_RAD_ANMO.AR_RECD_SEQN_NO%TYPE;
        lc_code1 sys_parm_mstr.pm_parm_code1%TYPE;
        lv_brnc_id  COM_RAD_ANMO.AR_BRNC_ID%TYPE;
        lv_viaje  COM_RAD_ANMO.AR_TRIP_NO%TYPE;
        LV_MOVIL  COM_RAD_ANMO.AR_TRUK_NO%TYPE;
        LV_RUTA  COM_RAD_ANMO.AR_ROUT_ID%TYPE;

    CURSOR cur_ftch_guia(LC_manifestNumber VARCHAR2) IS
           SELECT gh_guia_no,GH_CURR_LOCA,GH_CURR_DEST
             FROM BOK_GUIA_HEAD
               WHERE gh_guia_type = lc_h
                AND gh_actv_flag = lc_a
            AND gh_guia_refr_no = lc_manifestNumber;

    CURSOR cur_ftch_reas (mdul_id VARCHAR2, parm_type VARCHAR2, code2 VARCHAR2)IS
                SELECT PM_PARM_CODE1
                    FROM sys_parm_mstr
                    WHERE PM_MDUL_ID=mdul_id AND
                        PM_PARM_TYPE=parm_type AND
                        PM_PARM_CODE2=code2;

    BEGIN
         SELECT NVL(CT_TRNS_NO,'9999') INTO lc_transNo FROM COM_CTT
          WHERE CT_SECO_ENTY= manifestNumber
            AND CT_TRNS_TYPE = lc_iim
            AND CT_PRIM_ENTY = clientid;

         UPDATE COM_CTT SET CT_DATE_03 = SYSDATE
          WHERE CT_SECO_ENTY= manifestNumber
            AND CT_TRNS_TYPE = lc_iim
            AND CT_PRIM_ENTY = clientid;

        FOR i IN cur_ftch_guia(manifestNumber) LOOP
            SELECT NVL(MAX(GS_SERL_NO),0)+ 1
                  INTO lc_maxSerlNo
                  FROM BOK_GUIA_STUS
                  WHERE GS_GUIA_NO=i.gh_guia_no;--added by B.Emerson on 07/06/2003
            INSERT INTO BOK_GUIA_STUS
                    (GS_GUIA_NO,GS_STUS_CODE,GS_SERL_NO,GS_COM_TRNS_REF_CODE,
                     CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,GS_CTT_TRNS_TYPE,
                     GS_CURR_LOCA,GS_CURR_DEST)
                   VALUES(i.gh_guia_no,lc_rfm,lc_maxSerlNo,lc_transNo,
                               ld_date,userid,ld_date,userid,lc_rfm,
                          I.GH_CURR_LOCA,i.GH_CURR_DEST);

            /*INSERT INTO RAD_ORD_FOL
                    (RO_ORDER_ID, RO_GUIA_NO, RO_ACTV_FLAG, RO_REASON_ID,
                    CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, RO_DELETE_FLAG)
                   VALUES (manifestNumber, i.gh_guia_no, 'A', NULL, userid,
                          ld_date, userid, ld_date, 'N');*/

            /*added by B.Emerson on 07/06/2003*/
            UPDATE bok_guia_head SET gh_guia_stus = lc_rfm, gh_guia_refr_no = lc_null,
                MDFD_BY = userid, MDFD_ON = ld_date
                WHERE gh_guia_no = i.gh_guia_no AND
                gh_guia_type = lc_h AND
                gh_actv_flag = lc_a;
        END LOOP;
        --DELETE FROM WEB_MNFT_DETL WHERE MD_MNFT_NO = manifestNumber;
        UPDATE WEB_MNFT_DETL SET MD_MNFT_STUS= 'HCC', MD_DELETE_FLAG ='Y', MDFD_BY = userid, MDFD_ON = ld_date WHERE MD_MNFT_NO = manifestNumber;
        /*YA NO SE UTILIZA LA TABLA WEB_CTRL_EMAIL A NIVEL DE MANIFIESTO, AHORA ES POR GUIA*/
        --DELETE FROM WEB_CTRL_EMAIL WHERE CE_MNFT_NO = manifestNumber;--AAP02


        select TO_CHAR(AR_RECD_SEQN_NO.NEXTVAL)  into LV_RECD_SEQN_NO    from dual;

        OPEN cur_ftch_reas('RAD','RAD_RETURN',lc_amno_code);
        FETCH cur_ftch_reas INTO lc_code1;
        CLOSE cur_ftch_reas;

        SELECT MD_TRIP_NO, MD_ROUTE_ID, MD_TRUCK_NO
        INTO lv_viaje,LV_MOVIL,LV_RUTA
        FROM WEB_MNFT_DETL
        WHERE MD_MNFT_NO = manifestNumber;

        INSERT INTO COM_RAD_ANMO
                                                (AR_GRUP_ID
                                                ,AR_ANMO_ID
                                                ,AR_OHC_NO
                                                ,AR_CTT_REFR_NO
                                                ,AR_ANMO_DATE
                                                ,AR_COMT_BY
                                                ,AR_RECD_SEQN_NO
                                                ,AR_COMT
                                                ,AR_BRNC_ID
                                                ,AR_CTT_TRAN_TYPE
                                                ,AR_TRIP_NO
                                                ,AR_TRUK_NO
                                                ,AR_ROUT_ID
                                                ,CRTD_ON
                                                ,CRTD_BY
                                                ,MDFD_ON
                                                ,MDFD_BY)
                                    VALUES
                                                (lc_code1
                                                ,lc_amno_code
                                                ,manifestNumber
                                                ,lc_transNo
                                                ,sysdate--ld_ret_time
                                                ,userid
                                                ,LV_RECD_SEQN_NO
                                                ,'CANCELACION DESDE APP WEB'--lc_amno_comt
                                                ,lv_brnc_id
                                                ,lc_rfm
                                                ,lv_viaje
                                                ,LV_MOVIL
                                                ,LV_RUTA
                                                ,sysdate
                                                ,userid
                                                ,sysdate
                                                ,userid

                                                );


        COMMIT;
    END;
------------------------------------------------------------------------
/* For Web Booking */
/* This Function is used to Check whether RAD service is applicable to Origin Client Id */
/* Created By Venkat - 18/04/2003 */
FUNCTION FUN_CHCK_RAD_FLAG(clnt_id VARCHAR2)
RETURN VARCHAR2
IS
  li_flag CHAR(1);
  CURSOR cur_rad_flag(lv_clnt_id VARCHAR2) IS
    SELECT cm_rad_flag
      FROM SYS_CLNT_MSTR
     WHERE cm_clnt_id = lv_clnt_id;
BEGIN
    OPEN cur_rad_flag(clnt_id);
    FETCH cur_rad_flag INTO li_flag;
    IF cur_rad_flag%NOTFOUND THEN
        li_flag := '';
    END IF;
    CLOSE cur_rad_flag;
     RETURN li_flag;
END;
-------------------------------------------------------------------------
/* For Web Booking */
/* This Function is used to Check whether DEFAULT RAD service is applicable to Origin Group Client Id */
/* Created By Venkat - 18/04/2003 */
FUNCTION FUN_DFLT_RAD (CLIENT_ID VARCHAR2, SRVC VARCHAR2)
RETURN VARCHAR2
IS
    CURSOR CUR_DFLT_RAD(lv_clnt_id VARCHAR2,lv_srvc_id VARCHAR2) IS
    SELECT CS_DEFA_SRVC_ITEM
      FROM SYS_CLNT_SRVC
     WHERE CS_CLNT_ID = lv_clnt_id
       AND CS_SRVC_ID = LV_SRVC_ID;
    LC_RAD VARCHAR2(10);
BEGIN
  OPEN CUR_DFLT_RAD(CLIENT_ID,SRVC);
  FETCH CUR_DFLT_RAD INTO LC_RAD;
  IF CUR_DFLT_RAD%NOTFOUND THEN
      CLOSE CUR_DFLT_RAD;
      RETURN('FALSE');
  ELSE
      IF NVL(LC_RAD,' ') <> 'RAD-1' THEN
          CLOSE CUR_DFLT_RAD;
          RETURN('FALSE');
      END IF;
      CLOSE CUR_DFLT_RAD;
      RETURN('TRUE');
  END IF;
  CLOSE CUR_DFLT_RAD;
END;
-------------------------------------------------------------------------
/* For Web Booking */
/* This Function will return SUBTOTAL for the Addition Services */
FUNCTION FUN_FTCH_ADD_SRVC(SRVC_ID VARCHAR2, SRVC VARCHAR2,ORGN_BRNC VARCHAR2,
                       DEST_BRNC VARCHAR2,BRNC_ZONE VARCHAR2,
                       qty NUMBER,fact_type VARCHAR2, min_amt OUT NUMBER)
RETURN NUMBER
IS
         tarifid ACT_TRIF_ID.TI_TRIF_ID%TYPE;
        tariflvl ACT_TRIF_ID.TI_TRIF_LEVL%TYPE;
        tarifvltp ACT_TRIF_ID.TI_VLUE_TYPE%TYPE;
        tarifvlue ACT_TRIF.TR_TRIF_VLUE%TYPE;
        minvlue ACT_TRIF.TR_MIN_VLUE1%TYPE;
        maxvlue ACT_TRIF.TR_MAX_VLUE1%TYPE;
        temptarif NUMBER(12,2);
        tarvlue   NUMBER;
        factr_value NUMBER;

        CURSOR FTCH_ADD_SRVC_C1(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_FACT_TYPE VARCHAR2,lv_rownum NUMBER)
        IS
        SELECT ti_trif_id,ti_vlue_type
            FROM ACT_TRIF_ID
       WHERE ti_srvc_id = lv_srvc_id
            AND (TI_REFR_SRVC_ID = lv_srvc OR TI_REFR_SRVC_ID IS NULL)
         AND ti_fact          = lv_fact_type
         AND ROWNUM < lv_rownum;

      CURSOR FTCH_ADD_SRVC_C2(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_TARIFID VARCHAR2,
                                      LV_ORGN_BRNC VARCHAR2,LV_DEST_BRNC VARCHAR2,LV_BRNC_ZONE VARCHAR2)
      IS
      SELECT tr_trif_vlue,tr_min_vlue1,tr_max_vlue1
          FROM ACT_TRIF
        WHERE tr_srvc_id = lv_srvc_id
          AND (TR_REFR_SRVC_ID = lv_srvc OR TR_REFR_SRVC_ID IS NULL)
          AND tr_trif_id = lv_tarifid
          AND (tr_orgn_brnc = lv_ORGN_BRNC OR tr_orgn_brnc  IS NULL)
          AND (tr_dest_brnc = lv_DEST_BRNC OR tr_dest_brnc  IS NULL)
          AND (tr_clnt_zone = lv_BRNC_ZONE OR tr_clnt_zone  IS NULL);

     CURSOR FTCH_ADD_SRVC_C3_EAD(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_TARIFID VARCHAR2,
                                      LV_ORGN_BRNC VARCHAR2,LV_DEST_BRNC VARCHAR2,LV_BRNC_ZONE VARCHAR2)--AAP04
      IS
      SELECT tr_trif_vlue,tr_min_vlue1,tr_max_vlue1
          FROM ACT_TRIF
        WHERE tr_srvc_id = lv_srvc_id
          AND (TR_REFR_SRVC_ID = lv_srvc OR TR_REFR_SRVC_ID IS NULL)
          AND tr_trif_id = lv_tarifid
          AND (tr_orgn_brnc = lv_ORGN_BRNC OR tr_orgn_brnc  IS NULL)
          AND (SUBSTR (tr_dest_brnc,1,3) = SUBSTR(lv_DEST_BRNC,1,3) OR tr_dest_brnc  IS NULL)
          AND (tr_clnt_zone = lv_BRNC_ZONE OR tr_clnt_zone  IS NULL);
          
   CURSOR FTCH_ADD_SRVC_C4_EAD(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_TARIFID VARCHAR2,
                                      LV_ORGN_BRNC VARCHAR2,LV_DEST_BRNC VARCHAR2)--AAP08
      IS
      SELECT tr_trif_vlue,tr_min_vlue1,tr_max_vlue1 FROM
      (SELECT tr_trif_vlue,tr_min_vlue1,tr_max_vlue1
          FROM ACT_TRIF
        WHERE tr_srvc_id = lv_srvc_id
          AND (TR_REFR_SRVC_ID = lv_srvc OR TR_REFR_SRVC_ID IS NULL)
          AND tr_trif_id = lv_tarifid
          AND (tr_orgn_brnc = lv_ORGN_BRNC OR tr_orgn_brnc  IS NULL)
          AND (tr_dest_brnc = lv_DEST_BRNC OR tr_dest_brnc  IS NULL)
          ORDER BY tr_clnt_zone)
      WHERE ROWNUM = 1;       
BEGIN

    BEGIN
      OPEN FTCH_ADD_SRVC_C1(SRVC_ID,SRVC,FACT_TYPE,2);
      FETCH FTCH_ADD_SRVC_C1 INTO tarifid,tarifvltp;
      IF FTCH_ADD_SRVC_C1%NOTFOUND THEN
           tarvlue :=  0;
           RETURN(0);
    END IF;
      CLOSE FTCH_ADD_SRVC_C1;
    END;

  BEGIN
    IF SRVC = 'EAD' THEN --AAP04
        BEGIN
            --SE VALIDA SI EL DESTINO ES 70 PARA OBTENER EL COSTO SIN USAR ZONA DE EAD YA QUE EN OCASIONES NO ESTA CONFIGURADA CORRECTAMENTE.
            IF (SUBSTR (DEST_BRNC,4,2)='70') THEN--AAP08
                BEGIN
                    OPEN FTCH_ADD_SRVC_C4_EAD(SRVC_ID,SRVC,TARIFID,ORGN_BRNC,DEST_BRNC);--AAP08
                        FETCH FTCH_ADD_SRVC_C4_EAD INTO tarifvlue,minvlue,maxvlue;--AAP08
                        IF FTCH_ADD_SRVC_C4_EAD%NOTFOUND THEN--AAP08
                             tarifvlue := NULL;--AAP08
                             minvlue   := NULL;--AAP08
                             maxvlue   := NULL;--AAP08
                        END IF;--AAP08
                        CLOSE FTCH_ADD_SRVC_C4_EAD;--AAP08
                END;
            ELSE--AAP08
                BEGIN
                    OPEN FTCH_ADD_SRVC_C3_EAD(SRVC_ID,SRVC,TARIFID,ORGN_BRNC,DEST_BRNC,BRNC_ZONE);
                    FETCH FTCH_ADD_SRVC_C3_EAD INTO tarifvlue,minvlue,maxvlue;
                    IF FTCH_ADD_SRVC_C3_EAD%NOTFOUND THEN
                        tarifvlue := NULL;
                        minvlue   := NULL;
                        maxvlue   := NULL;
                    END IF;
                    CLOSE FTCH_ADD_SRVC_C3_EAD;
                END;
             END IF;        
        END;
    ELSE --AAP04
        BEGIN
            OPEN FTCH_ADD_SRVC_C2(SRVC_ID,SRVC,TARIFID,ORGN_BRNC,DEST_BRNC,BRNC_ZONE);
              FETCH FTCH_ADD_SRVC_C2 INTO tarifvlue,minvlue,maxvlue;
              IF FTCH_ADD_SRVC_C2%NOTFOUND THEN
                   tarifvlue := NULL;
                   minvlue   := NULL;
                   maxvlue   := NULL;
              END IF;
              CLOSE FTCH_ADD_SRVC_C2;
        END;
     END IF;
  END;

  IF tarifvlue IS NULL OR tarifvlue = 0 THEN
       NULL;
  END IF;
  tarvlue :=  tarifvlue;

  IF tarifvltp = 1 THEN
           RETURN(tarifvlue);
  ELSIF tarifvltp = 2 THEN
      RETURN(qty * tarifvlue);
  ELSIF tarifvltp = 3 THEN
      RETURN(qty * tarifvlue * factr_value);
  ELSIF tarifvltp = 4 THEN
      temptarif := (qty * (tarifvlue /100));

         IF minvlue IS NOT NULL OR maxvlue IS NOT NULL THEN
             IF minvlue IS NOT NULL THEN--added by B.Emerson on 18/07/2002
                  min_amt := minvlue;
             END IF;
          IF temptarif < minvlue THEN
              RETURN(minvlue);
          ELSIF temptarif > maxvlue THEN
              RETURN(maxvlue);
          ELSE
              RETURN(temptarif);
          END IF;
      END IF;
  ELSIF tarifvltp = 5 THEN
           NULL;
  END IF;
  RETURN(temptarif);
END;

-------------------------------------------------------------------------
/* For Web Booking */
/* This Function will return SUBTOTAL for the RAD Services*/

FUNCTION FUN_FTCH_ADD_RAD_SRVC(SRVC_ID VARCHAR2, SRVC VARCHAR2,ORGN_BRNC VARCHAR2,
                       DEST_BRNC VARCHAR2, qty NUMBER,
                       fact_type VARCHAR2, min_amt OUT NUMBER)
RETURN NUMBER
IS
         tarifid ACT_TRIF_ID.TI_TRIF_ID%TYPE;
        tariflvl ACT_TRIF_ID.TI_TRIF_LEVL%TYPE;
        tarifvltp ACT_TRIF_ID.TI_VLUE_TYPE%TYPE;
        tarifvlue ACT_TRIF.TR_TRIF_VLUE%TYPE;
        minvlue ACT_TRIF.TR_MIN_VLUE1%TYPE;
        maxvlue ACT_TRIF.TR_MAX_VLUE1%TYPE;
        temptarif NUMBER(12,2);
        tarvlue   NUMBER;
        factr_value NUMBER;

        CURSOR FTCH_ADD_SRVC_C1(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_FACT_TYPE VARCHAR2,lv_rownum NUMBER)
        IS
        SELECT ti_trif_id,ti_vlue_type
            FROM ACT_TRIF_ID
       WHERE ti_srvc_id = lv_srvc_id
            AND (TI_REFR_SRVC_ID = lv_srvc OR TI_REFR_SRVC_ID IS NULL)
         AND ti_fact          = lv_fact_type
         AND ROWNUM < lv_rownum;

      CURSOR FTCH_ADD_SRVC_C2(LV_SRVC_ID VARCHAR2,LV_SRVC VARCHAR2,LV_TARIFID VARCHAR2,
                                      LV_ORGN_BRNC VARCHAR2,LV_DEST_BRNC VARCHAR2)
      IS
      SELECT tr_trif_vlue,tr_min_vlue1,tr_max_vlue1
          FROM ACT_TRIF
        WHERE tr_srvc_id = lv_srvc_id
          AND (TR_REFR_SRVC_ID = lv_srvc OR TR_REFR_SRVC_ID IS NULL)
          AND tr_trif_id = lv_tarifid
          AND (substr(tr_orgn_brnc,1,3) = substr(lv_ORGN_BRNC,1,3) OR tr_orgn_brnc  IS NULL)--AAP04 SUBSTRING PARA OBTENER CONFIGURACION NIVEL SITE
          AND (tr_dest_brnc = lv_DEST_BRNC OR tr_dest_brnc  IS NULL);
BEGIN

    BEGIN
      OPEN FTCH_ADD_SRVC_C1(SRVC_ID,SRVC,FACT_TYPE,2);
      FETCH FTCH_ADD_SRVC_C1 INTO tarifid,tarifvltp;
      IF FTCH_ADD_SRVC_C1%NOTFOUND THEN
           tarvlue :=  0;
           RETURN(0);
    END IF;
      CLOSE FTCH_ADD_SRVC_C1;
    END;

  BEGIN
    OPEN FTCH_ADD_SRVC_C2(SRVC_ID,SRVC,TARIFID,ORGN_BRNC,DEST_BRNC );
      FETCH FTCH_ADD_SRVC_C2 INTO tarifvlue,minvlue,maxvlue;
      IF FTCH_ADD_SRVC_C2%NOTFOUND THEN
           tarifvlue := NULL;
           minvlue   := NULL;
           maxvlue   := NULL;
      END IF;
  END;

  IF tarifvlue IS NULL OR tarifvlue = 0 THEN
       NULL;
  END IF;
  tarvlue :=  tarifvlue;

  IF tarifvltp = 1 THEN
           RETURN(tarifvlue);
  ELSIF tarifvltp = 2 THEN
      RETURN(qty * tarifvlue);
  ELSIF tarifvltp = 3 THEN
      RETURN(qty * tarifvlue * factr_value);
  ELSIF tarifvltp = 4 THEN
      temptarif := (qty * (tarifvlue /100));

         IF minvlue IS NOT NULL OR maxvlue IS NOT NULL THEN
             IF minvlue IS NOT NULL THEN--added by B.Emerson on 18/07/2002
                  min_amt := minvlue;
             END IF;
          IF temptarif < minvlue THEN
              RETURN(minvlue);
          ELSIF temptarif > maxvlue THEN
              RETURN(maxvlue);
          ELSE
              RETURN(temptarif);
          END IF;
      END IF;
  ELSIF tarifvltp = 5 THEN
           NULL;
  END IF;
  RETURN(temptarif);
END FUN_FTCH_ADD_RAD_SRVC;

-------------------------------------------------------------------------
/* For Web Booking */
/* This Function is used to get the Discount against the Client Id defined */
FUNCTION FUN_GET_DISC_CLNT (ORGN_CLNT VARCHAR2, DEST_CLNT VARCHAR2, SRVC_ID VARCHAR2)
RETURN NUMBER
IS
    CURSOR GET_CLNT_DISC_CATGRY (CLNT_ID VARCHAR2, SRVC_ID VARCHAR2)    IS
      SELECT 1
        FROM SYS_CLNT_SRVC
        WHERE CS_CLNT_ID = CLNT_ID
          AND CS_SRVC_ID = SRVC_ID
          AND CS_CLNT_SRVC_CATY IS NOT NULL;

    L_ORGN_FLAG NUMBER := 0;
    L_DEST_FLAG NUMBER := 0;

BEGIN

  -- CHECK FOR ORIGIN CLIENT
  OPEN GET_CLNT_DISC_CATGRY (ORGN_CLNT, SRVC_ID);
  FETCH GET_CLNT_DISC_CATGRY INTO L_ORGN_FLAG;
  CLOSE GET_CLNT_DISC_CATGRY;

    -- CHECK FOR DESTINATION CLIENT
  OPEN GET_CLNT_DISC_CATGRY (DEST_CLNT, SRVC_ID);
  FETCH GET_CLNT_DISC_CATGRY INTO L_DEST_FLAG;
  CLOSE GET_CLNT_DISC_CATGRY;

    --CODE ADDED BY SATHISH ON 11/1/02
        IF L_ORGN_FLAG = 0 AND L_DEST_FLAG = 0 THEN
          RETURN 3; --'QUANTITY DISC';
        ELSIF L_ORGN_FLAG = 0 AND L_DEST_FLAG = 1 THEN
          RETURN 3; --'QUANTITY DISC';
        ELSIF L_ORGN_FLAG = 1 AND L_DEST_FLAG = 0 THEN
          RETURN 1; --'ORIGIN DISC';
        ELSIF L_ORGN_FLAG = 1 AND L_DEST_FLAG = 1 THEN
          RETURN 1; --'ORIGIN DISC';
        ELSE
          RETURN 3; --'QUANTITY DISC';
        END IF;
END;
------------------------------------------------------------------------
/* For Web Booking */
/* Function will return Guia No for the given Origin Branch Id passed
/* Guia Sequence Type is hardcoded as 'GUIASEQ' - Created By Venkat 18/04/2003 */

FUNCTION FUN_GEN_GUIA_NO(orgn_brnc_id VARCHAR2)
RETURN VARCHAR2
IS
NUM VARCHAR2(15);
ORG_NAME VARCHAR2(20);
SEQ VARCHAR2(15);
BRNC_SEQ VARCHAR2(15);
BEGIN
  Pack_Sipweb.Gen_Seqn_Num(ORGN_BRNC_ID,'GUIASEQ',NUM,BRNC_SEQ);
    ORG_NAME := BRNC_SEQ||NUM;
    RETURN ORG_NAME;
END;
---------------------------------------------------------
/* Check Guia sequence exists for prepared branch,
/* In parameter are branch id and sequence type ( Sequence Type will be hardcoded as 'GUIASEQ'*/
/* Created By Venkat - 18/04/2003 */
FUNCTION fun_guia_seq_exists(branch_id VARCHAR2) RETURN VARCHAR2 IS
    CURSOR cur_guia_seq(lv_brnc_id VARCHAR2,lv_seq_type VARCHAR2,lv_defa VARCHAR2) IS
        SELECT COUNT(*)
          FROM SYS_BRNC_SEQN
     WHERE BQ_BRNC_ID   = LTRIM(RTRIM(lv_brnc_id))
       AND BQ_SEQN_TYPE = LTRIM(RTRIM(lv_seq_type))
       AND BQ_DEFA_FLAG = lv_defa;
  ln_count NUMBER;
BEGIN
    OPEN cur_guia_seq(branch_id,'GUIASEQ','Y');
    FETCH cur_guia_seq INTO ln_count;
    IF ln_count = 0  THEN
         RETURN('FALSE');
    ELSE
         RETURN('TRUE');
    END IF;
    CLOSE cur_guia_seq;
END;

-------------------------------------------------------------
/* For Web Booking -- */
/* This Function is used to find whether the branch is either origin branch is  Non border or not
It is going to return 0 or 1 or 2 .
0 for 'INTERNAL GUIA' , 1 for 'BORDER GUIA ' and 2 for 'ALL'
 - Created By Emerson - 19/10/2006 */

FUNCTION fun_chk_brdr (site_id VARCHAR2, clnt_id VARCHAR2)
   RETURN NUMBER
IS
   CURSOR cur_brnc (brnc_id VARCHAR2, clnt_id VARCHAR2)
   IS
      SELECT gh_dest_brnc_id
        FROM bok_guia_head
       WHERE gh_orgn_clnt_id = clnt_id
         AND gh_orgn_brnc_id = brnc_id
         AND gh_guia_refr_no IS NULL
         AND GH_GUIA_TYPE ='H';

   CURSOR cur_brnc_in (brnc_id VARCHAR2, clnt_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'IN'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head
                          WHERE
                              GH_GUIA_TYPE ='H'
                              --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND GH_ACTV_FLAG = 'A'
                              AND gh_orgn_clnt_id = clnt_id
                            AND gh_orgn_brnc_id = brnc_id
                            AND gh_guia_refr_no IS NULL);


   CURSOR cur_brnc_br (brnc_id VARCHAR2, clnt_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'BR'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head
                          WHERE
                            GH_GUIA_TYPE ='H'
                            --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND GH_ACTV_FLAG = 'A'
                              AND gh_orgn_clnt_id = clnt_id
                            AND gh_orgn_brnc_id = brnc_id
                            AND gh_guia_refr_no IS NULL
                            );

   lc_brnc_loc   sys_brnc_mstr.bm_brnc_loc_type%TYPE;
   lc_brnc_br    NUMBER                                := 0;
   lc_brnc_in    NUMBER                                := 0;
   v_val         VARCHAR2 (20);
   brnc_id varchar2(100);

BEGIN


   SELECT bm_brnc_loc_type,bm_brnc_id
     INTO lc_brnc_loc,brnc_id
     FROM sys_brnc_mstr
    WHERE bm_brnc_id like site_id||'%'
     and rownum=1;


   IF lc_brnc_loc = 'IN'
   THEN
      RETURN 1;
   ELSE

      OPEN cur_brnc_br (brnc_id, clnt_id);

      FETCH cur_brnc_br
       INTO v_val;

      IF cur_brnc_br%FOUND
      THEN
         lc_brnc_br := 1;
      END IF;

      CLOSE cur_brnc_br;


      OPEN cur_brnc_in (brnc_id, clnt_id);

      FETCH cur_brnc_in
       INTO v_val;

      IF cur_brnc_in%FOUND
      THEN
         lc_brnc_in := 1;
      END IF;

      CLOSE cur_brnc_in;

      IF ((lc_brnc_br = 1) AND (lc_brnc_in = 0))
      THEN
      lc_brnc_br := 0;
         RETURN 1;
      ELSIF ((lc_brnc_br = 1) AND (lc_brnc_in = 1))
      THEN
      lc_brnc_br := 0;
      lc_brnc_in := 0;
         RETURN 2;
      ELSE
         RETURN 0;
      END IF;
   END IF;
END fun_chk_brdr;
FUNCTION fun_chk_brdr_new (site_id VARCHAR2, clnt_id VARCHAR2, crtd_by_user VARCHAR2)
   RETURN NUMBER
IS
   CURSOR cur_brnc (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
   IS
      SELECT gh_dest_brnc_id
        FROM bok_guia_head
       WHERE
         gh_orgn_brnc_id = brnc_id
         AND gh_orgn_clnt_id = clnt_id
         AND gh_guia_refr_no IS NULL
         AND GH_GUIA_TYPE ='H'
         AND GH_ACTV_FLAG ='A'
         AND GH_PYMT_MODE = 'PAID'
         AND GH_DOCU_TYPE = 'Q'
         AND CRTD_BY = user_id;

   CURSOR cur_brnc_in (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'IN'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head
                          WHERE
                            gh_orgn_brnc_id = brnc_id
                            AND gh_orgn_clnt_id = clnt_id
                            AND gh_guia_refr_no IS NULL
                            AND GH_GUIA_TYPE ='H'
                            AND GH_ACTV_FLAG ='A'
                            --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND GH_PYMT_MODE = 'PAID'
                            AND GH_DOCU_TYPE = 'Q'
                            AND CRTD_BY = user_id);

   CURSOR cur_brnc_br (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'BR'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head
                          WHERE
                            gh_orgn_brnc_id = brnc_id
                            AND gh_orgn_clnt_id = clnt_id
                            AND gh_guia_refr_no IS NULL
                            AND GH_GUIA_TYPE ='H'
                            AND GH_ACTV_FLAG ='A'
                            --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND GH_PYMT_MODE = 'PAID'
                            AND GH_DOCU_TYPE = 'Q'
                            AND CRTD_BY = user_id
                            );

   lc_brnc_loc   sys_brnc_mstr.bm_brnc_loc_type%TYPE;
   lc_brnc_br    NUMBER                                := 0;
   lc_brnc_in    NUMBER                                := 0;
   v_val         VARCHAR2 (20);
   brnc_id varchar2(100);

BEGIN


   SELECT bm_brnc_loc_type,bm_brnc_id
     INTO lc_brnc_loc,brnc_id
     FROM sys_brnc_mstr
    WHERE bm_brnc_id like site_id||'%'
     and rownum=1;


   IF lc_brnc_loc = 'IN'
   THEN
      RETURN 1;
   ELSE

      OPEN cur_brnc_br (brnc_id, clnt_id, crtd_by_user);

      FETCH cur_brnc_br
       INTO v_val;

      IF cur_brnc_br%FOUND
      THEN
         lc_brnc_br := 1;
      END IF;

      CLOSE cur_brnc_br;


      OPEN cur_brnc_in (brnc_id, clnt_id, crtd_by_user);

      FETCH cur_brnc_in
       INTO v_val;

      IF cur_brnc_in%FOUND
      THEN
         lc_brnc_in := 1;
      END IF;

      CLOSE cur_brnc_in;

      IF ((lc_brnc_br = 1) AND (lc_brnc_in = 0))
      THEN
      lc_brnc_br := 0;
         RETURN 1;
      ELSIF ((lc_brnc_br = 1) AND (lc_brnc_in = 1))
      THEN
      lc_brnc_br := 0;
      lc_brnc_in := 0;
         RETURN 2;
      ELSIF ((lc_brnc_br = 0) AND (lc_brnc_in = 1))
      THEN
         RETURN 3;
      ELSE
         RETURN 0;
      END IF;
   END IF;
END fun_chk_brdr_new;

FUNCTION fun_chk_brdr_new_ccosto (site_id VARCHAR2, clnt_id VARCHAR2, crtd_by_user VARCHAR2)
   RETURN NUMBER
IS
--   CURSOR cur_brnc (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
--   IS
--      SELECT DISTINCT gh_dest_brnc_id
--        FROM bok_guia_head a, bok_guia_head_extra b
--       WHERE
--         a.gh_orgn_brnc_id = brnc_id
--         AND a.gh_orgn_clnt_id = clnt_id
--         AND a.gh_guia_refr_no IS NULL
--         AND a.GH_GUIA_TYPE ='H'
--         AND a.GH_ACTV_FLAG ='A'
--         AND a.GH_PYMT_MODE = 'PAID'
--         AND a.GH_DOCU_TYPE = 'Q'
--         AND a.GH_GUIA_NO = b.GE_GUIA_NO
--         and b.GE_CLNT_CCOSTO_ID in (
--              SELECT CU_CCOSTO_ID
--                  FROM SYS_CLNT_CCOSTO_USER
--                 WHERE
--                  CU_CLNT_ID = clnt_id
--                  AND CU_USER_ID = user_id
--              );

   CURSOR cur_brnc_in (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'IN'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head a, bok_guia_head_extra b
                          WHERE
                            a.gh_orgn_brnc_id = brnc_id
                            AND a.gh_orgn_clnt_id = clnt_id
                            AND a.gh_guia_refr_no IS NULL
                            AND a.GH_GUIA_TYPE ='H'
                            AND a.GH_ACTV_FLAG ='A'
                            --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND a.GH_PYMT_MODE = 'PAID'
                            AND a.GH_DOCU_TYPE = 'Q'
                            AND a.GH_GUIA_NO = b.GE_GUIA_NO
                            and b.GE_CLNT_CCOSTO_ID in (
                                SELECT CU_CCOSTO_ID
                                    FROM SYS_CLNT_CCOSTO_USER
                                    WHERE
                                    CU_CLNT_ID = clnt_id
                                    AND CU_USER_ID = user_id
                                )
                );

   CURSOR cur_brnc_br (brnc_id VARCHAR2, clnt_id VARCHAR2, user_id VARCHAR2)
   IS
      SELECT bm_brnc_loc_type
        FROM sys_brnc_mstr
       WHERE bm_brnc_loc_type = 'BR'
         AND bm_brnc_id IN (
                SELECT DISTINCT gh_dest_brnc_id
                           FROM bok_guia_head a, bok_guia_head_extra b
                          WHERE
                            a.gh_orgn_brnc_id = brnc_id
                            AND a.gh_orgn_clnt_id = clnt_id
                            AND a.gh_guia_refr_no IS NULL
                            AND a.GH_GUIA_TYPE ='H'
                            AND a.GH_ACTV_FLAG ='A'
                            --and gh_guia_stus = 'WBK'--AAP01
                            --AND gh_guia_stus IN ('WBK','RFM','SWB')
                            AND gh_rad_flag IN ('1','6')
                            AND a.GH_PYMT_MODE = 'PAID'
                            AND a.GH_DOCU_TYPE = 'Q'
                            AND a.GH_GUIA_NO = b.GE_GUIA_NO
                            and b.GE_CLNT_CCOSTO_ID in (
                                SELECT CU_CCOSTO_ID
                                    FROM SYS_CLNT_CCOSTO_USER
                                    WHERE
                                    CU_CLNT_ID = clnt_id
                                    AND CU_USER_ID = user_id
                                )
                );

   lc_brnc_loc   sys_brnc_mstr.bm_brnc_loc_type%TYPE;
   lc_brnc_br    NUMBER                                := 0;
   lc_brnc_in    NUMBER                                := 0;
   v_val         VARCHAR2 (20);
   brnc_id varchar2(100);

BEGIN


   SELECT bm_brnc_loc_type,bm_brnc_id
     INTO lc_brnc_loc,brnc_id
     FROM sys_brnc_mstr
    WHERE bm_brnc_id like site_id||'%'
     and rownum=1;


   IF lc_brnc_loc = 'IN'
   THEN
      RETURN 1;
   ELSE

      OPEN cur_brnc_br (brnc_id, clnt_id, crtd_by_user);

      FETCH cur_brnc_br
       INTO v_val;

      IF cur_brnc_br%FOUND
      THEN
         lc_brnc_br := 1;
      END IF;

      CLOSE cur_brnc_br;


      OPEN cur_brnc_in (brnc_id, clnt_id, crtd_by_user);

      FETCH cur_brnc_in
       INTO v_val;

      IF cur_brnc_in%FOUND
      THEN
         lc_brnc_in := 1;
      END IF;

      CLOSE cur_brnc_in;

      IF ((lc_brnc_br = 1) AND (lc_brnc_in = 0))
      THEN
      lc_brnc_br := 0;
         RETURN 1;
      ELSIF ((lc_brnc_br = 1) AND (lc_brnc_in = 1))
      THEN
      lc_brnc_br := 0;
      lc_brnc_in := 0;
         RETURN 2;
      ELSIF ((lc_brnc_br = 0) AND (lc_brnc_in = 1))
      THEN
         RETURN 3;
      ELSE
         RETURN 0;
      END IF;
   END IF;
END fun_chk_brdr_new_ccosto;

----------------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure is used to INSERT RECORD(s) INTO TABLE BOK_GUIA_SRVC_ITEM_REQM
/* against the parameters defined - Created By Venkat 18/04/2003 */
PROCEDURE PRO_INSRT_WEB_BOK_ITEM_REQM(orgn_clnt_id IN VARCHAR2,addl_srvc_id IN VARCHAR2,
                                      guia_no IN VARCHAR2)
IS
REQM VARCHAR2 (5000):= '';
LV_COMT VARCHAR2 (1255) := NULL;
CURSOR CUR_REQM_TEXT(LV_INFO VARCHAR2) IS
  SELECT CR_SRVC_ID,CR_REFR_SRVC_ID,CR_TYPE,CR_CONF_FLAG,CR_DEFA_TEXT,
         CR_INFO_OR_REQM,CR_PRNT_IN_GUIA,CR_PRNT_IN_ACK,CR_BOOK_OR_DLVY_OR_BOTH, CR_SEQN_NO
        FROM SYS_CLNT_SRVC_REQM_MSTR
     WHERE CR_SRVC_ID      = addl_srvc_id
       AND CR_CLNT_ID      = orgn_clnt_id
         AND CR_INFO_OR_REQM = LV_INFO;
    BEGIN
      FOR I IN CUR_REQM_TEXT('R') LOOP
          BEGIN
            INSERT INTO BOK_GUIA_SRVC_ITEM_REQM (GI_SRVC_ID,GI_REFR_SRVC_ID,GI_GUIA_NO,
                                             GI_SEQ_NO,GI_REQM_TEXT,GI_CONF_FLAG,
                                                                                         GI_TYPE,
                                                                                         CRTD_ON,CRTD_BY,
                                                                                         MDFD_ON,MDFD_BY,
                                                                                         GI_SURC_FLAG,
                                                                                         GI_PRNT_IN_GUIA,
                                                                                         GI_PRNT_IN_ACK,
                                                                                         GI_BOOK_OR_DLRY )
                                      VALUES
                                             (I.CR_SRVC_ID,
                                                     I.CR_REFR_SRVC_ID,
                                                     GUIA_NO,
                                                                                         I.CR_SEQN_NO,--'1',
                                                                                         I.CR_DEFA_TEXT,
                                                                                            '1',
                                                     'R',
                                                     SYSDATE,
                                                                                           ORGN_CLNT_ID,
                                                       SYSDATE,
                                                                                           ORGN_CLNT_ID,
                                                                                           I.CR_INFO_OR_REQM,
                                                                                           I.CR_PRNT_IN_GUIA,
                                                                                           I.CR_PRNT_IN_ACK,
                                                                                           'T');
            REQM := REQM||I.CR_DEFA_TEXT||'.';
          EXCEPTION
              WHEN OTHERS THEN
                  NULL;
          END;
      END LOOP;
      
          /*ACTUALIZA COMENTARIOS CON REQUERIMIENTOS.*/
          IF REQM IS NOT NULL THEN
            REQM:='. REQ.ACUSE:'||REQM;
            SELECT GH_COMT INTO LV_COMT FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = GUIA_NO AND ROWNUM = 1;
            
            IF LV_COMT IS NULL THEN
                LV_COMT:= REQM;
            ELSE
                LV_COMT:= LV_COMT||REQM;
            END IF;
            
            IF LENGTH(LV_COMT)>255 THEN
                LV_COMT := SUBSTR (LV_COMT, 0, 255);
            END IF;
            
            UPDATE BOK_GUIA_HEAD
                SET GH_COMT = LV_COMT
                WHERE GH_GUIA_NO = GUIA_NO;
          END IF;
END;










----------------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure is used to get the Default Contact Details of the client - Created By Venkat 18/04/2003 */
PROCEDURE PRO_DEFA_CONT(orgn_addr_code IN VARCHAR2,orgn_clnt_id IN VARCHAR2,
                        cont_id OUT VARCHAR2,iden_type OUT VARCHAR2,
                        iden_no OUT VARCHAR2,cont_name OUT VARCHAR2) IS

    CURSOR cur_get_cont(lv_defa VARCHAR2,lv_enty_type VARCHAR2) IS
      SELECT CO_CONC_ID,CO_CONC_IDEN_TYPE_1,CO_CONC_IDEN_NO_1, CO_CONC_NAME
      FROM SYS_CONC_MSTR
     WHERE co_addr_code = orgn_addr_code
       AND co_defa_flag = lv_defa
       AND CO_ENTY_TYPE = lv_enty_type
       AND CO_ENTY_ID   = orgn_clnt_id;

  CURSOR cur_iden_type(lv_mdul VARCHAR2,lv_parm_type VARCHAR2,lv_iden_type VARCHAR2) IS
      SELECT pm_vlue1_desc
        FROM SYS_PARM_MSTR
          WHERE PM_PARM_CODE1 = lv_iden_type
            AND PM_MDUL_ID    = lv_mdul
           AND PM_PARM_TYPE  = lv_parm_type;
BEGIN
  OPEN cur_get_cont('Y','CLNT');
  FETCH cur_get_cont INTO cont_id,iden_type,iden_no,cont_name;
    IF cur_get_cont%NOTFOUND THEN
    --    cont_id := '';  Commented and Modified by Nirmala on 03/05/2003
        cont_id := orgn_clnt_id;
        iden_type := '';
        iden_no := '';
        cont_name:=NULL;
    END IF;
    CLOSE cur_get_cont;

 OPEN cur_iden_type('SYS','CLIENT_IDENT',iden_type);--iden_type);
    FETCH cur_iden_type INTO iden_type;
    IF cur_iden_type%NOTFOUND THEN
        iden_type := '';
    END IF;
    CLOSE cur_iden_type;
EXCEPTION WHEN OTHERS THEN
    cont_id   := orgn_clnt_id;
    iden_no   := NULL;
    iden_type := NULL;

END;
----------------------------------------------------------------------------------------
/* For Web Booking -- */
/* This Function is used to find whether the defined branch either for the
/* Origin or the Destination is a BORDER BRANCH  - Created By Venkat - 18/04/2003 */
FUNCTION fun_brdr_brnc(site_id VARCHAR2) RETURN VARCHAR2 IS
  brdr_type VARCHAR2(15);
--  CURSOR C1(lv_brnc_id VARCHAR2) IS
  CURSOR C1(site_id VARCHAR2) IS
    SELECT bm_brnc_loc_type
      FROM SYS_BRNC_MSTR
      -- WHERE bm_brnc_id = lv_brnc_id;
      WHERE bm_brnc_id like site_id||'%' and rownum=1;
BEGIN
--    OPEN C1(brnc_id);
    OPEN C1(site_id);
    FETCH c1 INTO brdr_type;
    IF c1%NOTFOUND THEN
        RETURN '';
    END IF;
        RETURN brdr_type;
  CLOSE c1;
END;
----------------------------------------------------------
/* For Web Booking */
/* This Procedure will INSERT RECORD(s) INTO Table BOK_GUIA_STUS - Created By Venkat - 18/04/2003 */
PROCEDURE PRO_INSRT_WEB_BOK_STUS (GUIA_NO VARCHAR2,SHIP_SEQN_NO NUMBER,NO_SHIP NUMBER,
                                  CUR_LOC VARCHAR2, CUR_DEST VARCHAR2,ORGN_CLNT_ID VARCHAR2)
IS
    LV_WBK VARCHAR2(5) := 'WBK';
    LV_SEQ NUMBER      := 1;
BEGIN
    INSERT INTO BOK_GUIA_STUS(GS_GUIA_NO,
                            GS_STUS_CODE,
                            GS_SERL_NO,
                            GS_SHIP_SEQN_NO,
                            GS_NO_SHIP,
                            GS_CURR_LOCA,
                            GS_CURR_DEST,
                            CRTD_ON,
                            CRTD_BY,
                               MDFD_ON,
                            MDFD_BY)
         VALUES(GUIA_NO,
              LV_WBK,
              LV_SEQ,
              SHIP_SEQN_NO,
                NO_SHIP,
              CUR_LOC,
              CUR_DEST,
              SYSDATE,
              ORGN_CLNT_ID,
                SYSDATE,
              ORGN_CLNT_ID);

END;

-------------------------------------------------------------------------------------------
/* For Web Booking */
/* This Procedure will INSERT RECORD(s) INTO Table BOK_GUIA_ADDR
/* against the following IN parameters  - Created By Venkat - 18/04/2003 */
PROCEDURE PRO_INSRT_WEB_BOK_ADDR(guia_no VARCHAR2, clnt_id VARCHAR2, addr_code_orgn VARCHAR2,
    addr_code_dest VARCHAR2, addr_code_fisc VARCHAR2) IS
    CURSOR cur_ftch_addr (addr_code VARCHAR2)IS
        SELECT am_strt_name, am_drnr, am_phno1, am_flor_no, am_suit_no,
            am_addr_defn_type, am_addr_ref_no, am_gety_code, am_gety_levl, am_gety_type
            FROM SYS_ADDR_MSTR
            WHERE am_addr_code = addr_code;
    lc_am_strt_name             SYS_ADDR_MSTR.am_strt_name%TYPE;
    lc_am_drnr                         SYS_ADDR_MSTR.am_drnr%TYPE;
    lc_am_phno1                     SYS_ADDR_MSTR.am_phno1%TYPE;
    lc_am_flor_no                 SYS_ADDR_MSTR.am_flor_no%TYPE;
    lc_am_suit_no                 SYS_ADDR_MSTR.am_suit_no%TYPE;
    lc_am_addr_defn_type     SYS_ADDR_MSTR.am_addr_defn_type%TYPE;
    lc_am_addr_ref_no         SYS_ADDR_MSTR.am_addr_ref_no%TYPE;
    lc_am_gety_code             SYS_ADDR_MSTR.am_gety_code%TYPE;
    lc_am_gety_levl             SYS_ADDR_MSTR.am_gety_levl%TYPE;
    lc_am_gety_type             SYS_ADDR_MSTR.am_gety_type%TYPE;
    lv_orgn VARCHAR2(30) := 'ORIGIN';
    lv_dest VARCHAR2(30) := 'DESTINATION';
    lv_fisc VARCHAR2(30) := 'FISCAL';
    LV_N VARCHAR2(5)     := 'N';
    LV_C VARCHAR2(5)     := 'C';
    LV_DATE DATE         := SYSDATE;
    LV_NULL VARCHAR2(10):= '';
    u11 VARCHAR2(50);    u12 VARCHAR2(50);    u13 VARCHAR2(50);
    u14 VARCHAR2(50);    u15 VARCHAR2(50);    u16 VARCHAR2(60);    u17 VARCHAR2(50);    zipcode VARCHAR2(50);
    c11 VARCHAR2(50);    c12 VARCHAR2(50);    c13 VARCHAR2(50);
    c14 VARCHAR2(50);    c15 VARCHAR2(50);    c16 VARCHAR2(50);
BEGIN
    IF addr_code_orgn IS NOT NULL THEN
        OPEN cur_ftch_addr(addr_code_orgn);
        FETCH cur_ftch_addr INTO lc_am_strt_name, lc_am_drnr, lc_am_phno1, lc_am_flor_no,
            lc_am_suit_no, lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code,
            lc_am_gety_levl, lc_am_gety_type;
        CLOSE cur_ftch_addr;
        pro_ftch_addr(lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code, lc_am_gety_levl,
            lc_am_gety_type, u11, u12, u13, u14, u15, u16, u17, zipcode,
            c11, c12, c13, c14, c15, c16);
        INSERT INTO BOK_GUIA_ADDR (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
        GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
            GA_ADDR_LIN4,GA_ADDR_LIN5,
            GA_ADDR_LIN6,GA_ADDR_LIN7,
            CRTD_ON,CRTD_BY,
            MDFD_ON,MDFD_BY,
            GA_TO_PRIN,
            GA_ADDR_CODE,
        GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
            GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
        GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
        GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
    VALUES                    (guia_no,LV_C,lv_orgn,
      u11,u12,u13,/*AAP06*/u15,
      /*AAP06*/u14,u16,u17,
      LV_DATE,clnt_id,
      LV_DATE,clnt_id,
      LV_N,
      addr_code_orgn,
      lc_am_strt_name, lc_am_drnr,lc_am_phno1, lc_am_flor_no, lc_am_suit_no,zipcode,
      c11, c12, c13, /*AAP06*/c15, /*AAP06*/c14, c16,LV_NULL);
    END IF;
    IF addr_code_dest IS NOT NULL THEN
        OPEN cur_ftch_addr(addr_code_dest);
        FETCH cur_ftch_addr INTO lc_am_strt_name, lc_am_drnr, lc_am_phno1, lc_am_flor_no,
            lc_am_suit_no, lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code,
            lc_am_gety_levl, lc_am_gety_type;
        CLOSE cur_ftch_addr;
        pro_ftch_addr(lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code, lc_am_gety_levl,
            lc_am_gety_type, u11, u12, u13, u14, u15, u16, u17, zipcode,
            c11, c12, c13, c14, c15, c16);
        INSERT INTO BOK_GUIA_ADDR (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
        GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
            GA_ADDR_LIN4,GA_ADDR_LIN5,
            GA_ADDR_LIN6,GA_ADDR_LIN7,
            CRTD_ON,CRTD_BY,
            MDFD_ON,MDFD_BY,
            GA_TO_PRIN,
            GA_ADDR_CODE,
        GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
            GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
        GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
        GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
    VALUES                    (guia_no,LV_C,lv_dest,
      u11,u12,u13,/*AAP06*/u15,
      /*AAP06*/u14,u16,u17,
      LV_DATE,clnt_id,
      LV_DATE,clnt_id,
      LV_N,
      addr_code_dest,
      lc_am_strt_name, lc_am_drnr,lc_am_phno1, lc_am_flor_no, lc_am_suit_no,zipcode,
      c11, c12, c13, c14, c15, c16,LV_NULL);
    END IF;
    IF addr_code_fisc IS NOT NULL THEN
        OPEN cur_ftch_addr(addr_code_fisc);
        FETCH cur_ftch_addr INTO lc_am_strt_name, lc_am_drnr, lc_am_phno1, lc_am_flor_no,
            lc_am_suit_no, lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code,
            lc_am_gety_levl, lc_am_gety_type;
        CLOSE cur_ftch_addr;
        pro_ftch_addr(lc_am_addr_defn_type, lc_am_addr_ref_no, lc_am_gety_code, lc_am_gety_levl,
            lc_am_gety_type, u11, u12, u13, u14, u15, u16, u17, zipcode,
            c11, c12, c13, c14, c15, c16);
        INSERT INTO BOK_GUIA_ADDR (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
        GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
            GA_ADDR_LIN4,GA_ADDR_LIN5,
            GA_ADDR_LIN6,GA_ADDR_LIN7,
            CRTD_ON,CRTD_BY,
            MDFD_ON,MDFD_BY,
            GA_TO_PRIN,
            GA_ADDR_CODE,
        GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
            GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
        GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
        GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
    VALUES                    (guia_no,LV_C,lv_fisc,
      u11,u12,u13,/*AAP06*/u15,
      /*AAP06*/u14, u16,u17,
      LV_DATE,clnt_id,
      LV_DATE,clnt_id,
      LV_N,
      addr_code_fisc,
      lc_am_strt_name, lc_am_drnr,lc_am_phno1, lc_am_flor_no, lc_am_suit_no,zipcode,
      c11, c12, c13, /*AAP06*/c15, /*AAP06*/c14, c16,LV_NULL);
   END IF;

      -- Actualizo las direcciones FISCAL y ORIGIN     --  Hugo Valdez  07/11/2011
   BEGIN
      UPDATE BOK_GUIA_ADDR
      SET
      --GA_ADDR_LIN1 = NIVEL_123456(GA_ADDR_COD6,'1'),
      GA_ADDR_LIN1 = (SELECT PAIS FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN2 = NIVEL_123456(GA_ADDR_COD6,'2'),
      GA_ADDR_LIN2 = (SELECT ZONA FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN3 = NIVEL_123456(GA_ADDR_COD6,'3')
      GA_ADDR_LIN3 = (SELECT ESTADO FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6)
      WHERE
      GA_GUIA_NO = guia_no AND GA_ADDR_TYPE='ORIGIN';

      UPDATE BOK_GUIA_ADDR
      SET
     --GA_ADDR_LIN1 = NIVEL_123456(GA_ADDR_COD6,'1'),
      GA_ADDR_LIN1 = (SELECT PAIS FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN2 = NIVEL_123456(GA_ADDR_COD6,'2'),
      GA_ADDR_LIN2 = (SELECT ZONA FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN3 = NIVEL_123456(GA_ADDR_COD6,'3')
      GA_ADDR_LIN3 = (SELECT ESTADO FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6)
      WHERE
      GA_GUIA_NO = guia_no AND GA_ADDR_TYPE='DESTINATION';

      UPDATE BOK_GUIA_ADDR
      SET
      --GA_ADDR_LIN1 = NIVEL_123456(GA_ADDR_COD6,'1'),
      GA_ADDR_LIN1 = (SELECT PAIS FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN2 = NIVEL_123456(GA_ADDR_COD6,'2'),
      GA_ADDR_LIN2 = (SELECT ZONA FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6),
      --GA_ADDR_LIN3 = NIVEL_123456(GA_ADDR_COD6,'3')
      GA_ADDR_LIN3 = (SELECT ESTADO FROM PCOBERTURA_VIEW WHERE COD_COLO = GA_ADDR_COD6)
      WHERE
      GA_GUIA_NO = guia_no AND GA_ADDR_TYPE='FISCAL';

   END;

END;

/*PROCEDURE PRO_INSRT_WEB_BOK_ADDR(addr_code IN VARCHAR2,addr_type IN VARCHAR2,
                                 guia_no        IN VARCHAR2,orgn_clnt_id   IN VARCHAR2,
                                 addr_line1     IN VARCHAR2,addr_line2 IN VARCHAR2,addr_line3     IN VARCHAR2,
                                 addr_line4     IN VARCHAR2,addr_line5 IN VARCHAR2,addr_line6     IN VARCHAR2,
                                 addr_line7     IN VARCHAR2,
                                 str_name IN VARCHAR2, drnr IN VARCHAR2,ph_no IN VARCHAR2,
                                 fl_no IN VARCHAR2, suit_no IN VARCHAR2,zipcode IN VARCHAR2,
                                 addr_cod1 IN VARCHAR2,addr_cod2 IN VARCHAR2,addr_cod3 IN VARCHAR2,
                                 addr_cod4 IN VARCHAR2,addr_cod5 IN VARCHAR2,addr_cod6 IN VARCHAR2)
IS
lv_orgn varchar2(30) := 'ORIGIN';
lv_dest varchar2(30) := 'DESTINATION';
lv_fisc varchar2(30) := 'FISCAL';
LV_N VARCHAR2(5)     := 'N';
LV_C VARCHAR2(5)     := 'C';
LV_DATE DATE         := SYSDATE;
LV_NULL VARCHAR2(10):= '';
BEGIN
    if addr_code is not null and addr_type = 'ORIGIN' then
         INSERT INTO bok_guia_addr (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
                                GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
                                                              GA_ADDR_LIN4,GA_ADDR_LIN5,
                                                                GA_ADDR_LIN6,GA_ADDR_LIN7,
                                                                CRTD_ON,CRTD_BY,
                                                                MDFD_ON,MDFD_BY,
                                                                GA_TO_PRIN,
                                                                GA_ADDR_CODE,
                                GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
                                                                 GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
                                GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
                                GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
                        VALUES
                               (GUIA_NO,LV_C,ADDR_TYPE,
                                ADDR_LINE1,ADDR_LINE2,ADDR_LINE3,ADDR_LINE4,
                                ADDR_LINE5,ADDR_LINE6,ADDR_LINE7,
                                LV_DATE,ORGN_CLNT_ID,
                                LV_DATE,ORGN_CLNT_ID,
                                LV_N,
                                ADDR_CODE,
                                STR_NAME, DRNR,PH_NO, FL_NO, SUIT_NO,ZIPCODE,
                                ADDR_COD1,ADDR_COD2,ADDR_COD3,ADDR_COD4,ADDR_COD5,ADDR_COD6,LV_NULL);
    end if;

    if addr_code is not null and addr_type = 'DESTINATION' then
         INSERT INTO bok_guia_addr (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
                                GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
                                                              GA_ADDR_LIN4,GA_ADDR_LIN5,
                                                                GA_ADDR_LIN6,GA_ADDR_LIN7,
                                                                CRTD_ON,CRTD_BY,
                                                                MDFD_ON,MDFD_BY,
                                                                GA_TO_PRIN,
                                                                GA_ADDR_CODE,
                                GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
                                                                 GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
                                GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
                                GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
                            VALUES
                                   (GUIA_NO,LV_C,ADDR_TYPE,
                                    ADDR_LINE1,ADDR_LINE2,ADDR_LINE3,ADDR_LINE4,
                                    ADDR_LINE5,ADDR_LINE6,ADDR_LINE7,
                                      LV_DATE,ORGN_CLNT_ID,
                                      LV_DATE,ORGN_CLNT_ID,
                                      LV_N,
                                      ADDR_CODE,
                                      STR_NAME, DRNR,PH_NO, FL_NO, SUIT_NO,ZIPCODE,
                                      ADDR_COD1,ADDR_COD2,ADDR_COD3,ADDR_COD4,ADDR_COD5,ADDR_COD6,LV_NULL);
    end if;
    if addr_code is not null and addr_type = 'FISCAL' then
         INSERT INTO bok_guia_addr (GA_GUIA_NO,GA_ADDR_STAT,GA_ADDR_TYPE,
                                GA_ADDR_LIN1,GA_ADDR_LIN2,GA_ADDR_LIN3,
                                                              GA_ADDR_LIN4,GA_ADDR_LIN5,
                                                                GA_ADDR_LIN6,GA_ADDR_LIN7,
                                                                CRTD_ON,CRTD_BY,
                                                                MDFD_ON,MDFD_BY,
                                                                GA_TO_PRIN,
                                                                GA_ADDR_CODE,
                                GA_STRT_NAME,GA_DRNR,GA_PHNO_1,
                                                                 GA_FLOR_NO,GA_SUIT_NO,GA_ZIP_CODE,
                                GA_ADDR_COD1,GA_ADDR_COD2,GA_ADDR_COD3,GA_ADDR_COD4,
                                GA_ADDR_COD5,GA_ADDR_COD6,GA_ADDR_COD7)
                             VALUES
                                    (GUIA_NO,LV_C,addr_type,
                                     ADDR_LINE1,ADDR_LINE2,ADDR_LINE3,ADDR_LINE4,
                                     ADDR_LINE5,ADDR_LINE6,ADDR_LINE7,
                                       LV_DATE,ORGN_CLNT_ID,
                                       LV_DATE,ORGN_CLNT_ID,
                                       LV_N,
                                       ADDR_CODE,
                                       STR_NAME, DRNR,PH_NO, FL_NO, SUIT_NO,ZIPCODE,
                                       ADDR_COD1,ADDR_COD2,ADDR_COD3,ADDR_COD4,ADDR_COD5,ADDR_COD6,LV_NULL);
    end if;

END;*/
--------------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure will INSERT SERVICE ITEM  into TABLE BOK_GUIA_SRVC_ITEM against
/* each service Defined  - Created By Venkat 18/04/2003 */
PROCEDURE PRO_INSRT_WEB_BOK_SRVC_ITEM (GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,REFR_SRVC IN VARCHAR2,
                                       DESCR IN VARCHAR2,CONTENT IN VARCHAR2,
                                       QTY IN VARCHAR2,SRVC_CHRG IN NUMBER,
                                       STUS_FLAG IN VARCHAR2,
                                       CREATD_ON IN DATE,CREATD_BY IN VARCHAR2,
                                       MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                       SLAB IN VARCHAR2,
                                       WEIGHT IN NUMBER,VOLUME IN NUMBER,
                                       DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2) IS
BEGIN
  INSERT INTO BOK_GUIA_SRVC_ITEM(GL_GUIA_NO,GL_SRVC_ID,GL_REFR_SRVC_ID,GL_DESC,
                                 GL_CONT,GL_QUNT,GL_SRVC_CHRG,
                                 GL_STUS_FLAG,
                                 CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,
                                 GL_SLAB_NO,
                                 GL_VLUE_1,GL_VLUE_2,
                                 GL_DOCU_TYPE,GL_GUIA_TYPE)
                          VALUES
                                (GUIA_NO,SRVC,REFR_SRVC,
                                 DESCR,CONTENT,QTY,
                                 ROUND(NVL(SRVC_CHRG,0),2),
                                 STUS_FLAG,
                                 CREATD_ON,CREATD_BY,MODI_ON,MODI_BY,
                                 SLAB,
                                 ROUND(NVL(WEIGHT,0),2),
                                 ROUND(NVL(VOLUME,0),2),
                                 DOCU_TYPE,GUIA_TYPE);

END;
--------------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure will INSERT SERVICE ITEM  into TABLE WEB_BOK_GUIA_SRVC_ITEM against
/* each service Defined  - Created By EMERSON 19/12/2006 */
PROCEDURE PRO_INSRT_WEB_BOK_SRVC_ITEM_NE (GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,REFR_SRVC IN VARCHAR2,
                                       DESCR IN VARCHAR2,CONTENT IN VARCHAR2,
                                       QTY IN VARCHAR2,SRVC_CHRG IN NUMBER,
                                       STUS_FLAG IN VARCHAR2,
                                       CREATD_ON IN DATE,CREATD_BY IN VARCHAR2,
                                       MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                       SLAB IN VARCHAR2,
                                       WEIGHT IN NUMBER,VOLUME IN NUMBER,
                                       DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2) IS
BEGIN
  INSERT INTO WEB_BOK_GUIA_SRVC_ITEM(W_GL_GUIA_NO,W_GL_SRVC_ID,W_GL_REFR_SRVC_ID,W_GL_DESC,
                                 W_GL_CONT,W_GL_QUNT,W_GL_SRVC_CHRG,
                                 W_GL_STUS_FLAG,
                                 W_CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,
                                 W_GL_SLAB_NO,
                                 W_GL_VLUE_1,W_GL_VLUE_2,
                                 W_GL_DOCU_TYPE,W_GL_GUIA_TYPE)
                          VALUES
                                (GUIA_NO,SRVC,REFR_SRVC,
                                 DESCR,CONTENT,QTY,
                                 ROUND(NVL(SRVC_CHRG,0),2),
                                 STUS_FLAG,
                                 CREATD_ON,CREATD_BY,MODI_ON,MODI_BY,
                                 SLAB,
                                 ROUND(NVL(WEIGHT,0),2),
                                 ROUND(NVL(VOLUME,0),2),
                                 DOCU_TYPE,GUIA_TYPE);
END;
--------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure will INSERT RECORD(s) INTO TABLE bok_guia_srvc against the parameters */
/* Created By Venkat - 18/04/2003 */
PROCEDURE PRO_INSRT_WEB_BOK_SRVC(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,DISC IN NUMBER,
                                 TAX IN NUMBER,TAX_RET IN NUMBER,AMOUNT IN NUMBER ,
                                 FLAG IN VARCHAR2,SRVC_TYPE IN VARCHAR2,
                                 DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2,
                                 CRETD_ON IN DATE,CRETD_BY IN VARCHAR2,
                                 MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                 STUS_FLAG IN VARCHAR2,DISC_SLAB IN VARCHAR2)IS
BEGIN
     INSERT INTO BOK_GUIA_SRVC(GS_GUIA_NO,
                               GS_SRVC_ID,
                               GS_DISC,
                               GS_TAX,
                               GS_TAX_RET,
                               GS_SUB_TOTL,
                               GS_ADD_PYMT_FLAG,
                               GS_SRVC_TYPE,
                               GS_DOCU_TYPE,
                               GS_GUIA_TYPE,
                               CRTD_ON,
                               CRTD_BY,
                               MDFD_ON,
                               MDFD_BY,
                               GS_STUS_FLAG,
                               GS_DISC_SLAB_NO)
                        VALUES
                               (GUIA_NO,
                                SRVC,
                                ROUND(NVL(DISC,0),2),
                                ROUND(NVL(TAX,0),2),
                                ROUND(NVL(TAX_RET,0),2),
                                ROUND(NVL(AMOUNT,0),2),
                                FLAG,
                                SRVC_TYPE,
                                DOCU_TYPE,
                                GUIA_TYPE,
                                CRETD_ON,
                                CRETD_BY,
                                MODI_ON,
                                MODI_BY,
                                STUS_FLAG,
                                DISC_SLAB);
END;
--------------------------------------------------------------------------------------
/* For Web Booking */
/* This procedure will INSERT RECORD(s) INTO TABLE WEB_bok_guia_srvc against the parameters */
/* Created By EMERSON - 19/12/2006 */
PROCEDURE PRO_INSRT_WEB_BOK_SRVC_NEW(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,DISC IN NUMBER,
                                 TAX IN NUMBER,TAX_RET IN NUMBER,AMOUNT IN NUMBER ,
                                 FLAG IN VARCHAR2,SRVC_TYPE IN VARCHAR2,
                                 DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2,
                                 CRETD_ON IN DATE,CRETD_BY IN VARCHAR2,
                                 MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                 STUS_FLAG IN VARCHAR2,DISC_SLAB IN VARCHAR2)IS
BEGIN
     INSERT INTO WEB_BOK_GUIA_SRVC(W_GS_GUIA_NO,
                               W_GS_SRVC_ID,
                               W_GS_DISC,
                               W_GS_TAX,
                               W_GS_TAX_RET,
                               W_GS_SUB_TOTL,
                               W_GS_ADD_PYMT_FLAG,
                               W_GS_SRVC_TYPE,
                               W_GS_DOCU_TYPE,
                               W_GS_GUIA_TYPE,
                               CRTD_ON,
                               CRTD_BY,
                               MDFD_ON,
                               MDFD_BY,
                               W_GS_STUS_FLAG,
                               W_GS_DISC_SLAB_NO)
                        VALUES
                               (GUIA_NO,
                                SRVC,
                                ROUND(NVL(DISC,0),2),
                                ROUND(NVL(TAX,0),2),
                                ROUND(NVL(TAX_RET,0),2),
                                ROUND(NVL(AMOUNT,0),2),
                                FLAG,
                                SRVC_TYPE,
                                DOCU_TYPE,
                                GUIA_TYPE,
                                CRETD_ON,
                                CRETD_BY,
                                MODI_ON,
                                MODI_BY,
                                STUS_FLAG,
                                DISC_SLAB);
END;
-----------------------
/* For Web Booking -- */
/* This Function is used to get the Delivery Type Availability either for the
   Origin Client Group Id or the Origin Client Id -- Created by Venkat on 18/04/2003 */
FUNCTION FUN_DFLT_DLVY(CLIENT_ID VARCHAR2, SRVC VARCHAR2) RETURN VARCHAR2 IS
    CURSOR CUR_DFLT_DLVY(lv_clnt_id VARCHAR2,lv_srvc_id VARCHAR2) IS
      SELECT CS_DEFA_SRVC_ITEM
        FROM SYS_CLNT_SRVC
       WHERE CS_CLNT_ID = lv_clnt_id
         AND CS_SRVC_ID = LV_SRVC_ID;

     LV_CS_DEFA_SRVC SYS_CLNT_SRVC.CS_DEFA_SRVC_ITEM%TYPE;
    BEGIN

     OPEN CUR_DFLT_DLVY(CLIENT_ID,SRVC);
     FETCH CUR_DFLT_DLVY INTO  LV_CS_DEFA_SRVC;
      IF CUR_DFLT_DLVY%NOTFOUND THEN
         RETURN('FALSE');
      ELSE
           RETURN('TRUE');
      END IF;
      CLOSE CUR_DFLT_DLVY;
END;
------------------
/* For WEB Booking */
/* This Procedure is used to Check whether service 'EAD' is applicable to the
   Destination City, Colony and Branch Selected  - Created by Venkat on 18/04/2003 */
PROCEDURE pro_brnc_coly(city VARCHAR2,colonia VARCHAR2,branch_id VARCHAR2,
                       lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2) IS
     CURSOR cur_zone(lv_city VARCHAR2,lv_colonia VARCHAR2,lv_brnc_id VARCHAR2) IS
      SELECT bc_brnc_id,BC_ZONE
        FROM ER_BRNC_COLY_MSTR
       WHERE --BC_GETY_CODE_4 = lv_city
         --AND 
         BC_GETY_CODE_6 = lv_colonia
         AND bc_brnc_id     = lv_brnc_id;
    lc_brnc VARCHAR2(80);
    flg NUMBER := 0;
BEGIN
    FOR i IN cur_zone(city,colonia,branch_id) LOOP
        lc_brnc := i.bc_brnc_id;
        lc_zone := i.BC_ZONE;
        flg := 1;
    END LOOP;
      IF flg = 0 THEN
       lc_boolean := 'FALSE';
    ELSE
         lc_boolean := 'TRUE';
      END IF;
END;
------------------------------------------
PROCEDURE pro_brnc_coly_ze(city VARCHAR2,colonia VARCHAR2,branch_id VARCHAR2,
                       lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2, lc_ol OUT VARCHAR2) IS
     CURSOR cur_zone(lv_city VARCHAR2,lv_colonia VARCHAR2,lv_brnc_id VARCHAR2, lv_ol VARCHAR2) IS
      SELECT bc_brnc_id, BC_ZONE, BC_OL_ID
        FROM ER_BRNC_COLY_MSTR
       WHERE --BC_GETY_CODE_4 = lv_city
         --AND 
         BC_GETY_CODE_6 = lv_colonia
         AND bc_brnc_id     = lv_brnc_id;
    lc_brnc VARCHAR2(80);
    flg NUMBER := 0;
BEGIN
    FOR i IN cur_zone(city,colonia,branch_id, lc_ol) LOOP
        lc_brnc := i.bc_brnc_id;
        lc_zone := i.BC_ZONE;
        lc_ol := i.BC_OL_ID;
        flg := 1;
    END LOOP;
      IF flg = 0 THEN
       lc_boolean := 'FALSE';
    ELSE
         lc_boolean := 'TRUE';
      END IF;
END;
PROCEDURE pro_site_coly(city VARCHAR2,colonia VARCHAR2, site_id VARCHAR2,
                       lc_zone OUT VARCHAR2, lc_boolean OUT VARCHAR2) IS
     CURSOR cur_zone(lv_city VARCHAR2,lv_colonia VARCHAR2,lv_site_id VARCHAR2) IS
      SELECT bc_brnc_id,BC_ZONE
        FROM ER_BRNC_COLY_MSTR
       WHERE --BC_GETY_CODE_4 = lv_city
         --AND 
         BC_GETY_CODE_6 = lv_colonia
         AND substr(bc_brnc_id,1,3) = lv_site_id;
    lc_brnc VARCHAR2(80);
    flg NUMBER := 0;
BEGIN
    FOR i IN cur_zone(city,colonia,site_id) LOOP
        lc_brnc := i.bc_brnc_id;
        lc_zone := i.BC_ZONE;
        flg := 1;
    END LOOP;
      IF flg = 0 THEN
       lc_boolean := 'FALSE';
    ELSE
         lc_boolean := 'TRUE';
      END IF;
END;
------------------------------------------
PROCEDURE pro_ship_dest_mstr(orgn_BRNC VARCHAR2,dest_BRNC VARCHAR2,ship_seqn OUT NUMBER,
                             no_ship OUT NUMBER,cur_loc OUT VARCHAR2,
                             cur_dest OUT VARCHAR2,lc_rout OUT VARCHAR2) IS
      CURSOR C1(lv_pref NUMBER,lv_seq NUMBER) IS
        SELECT ship_seq, no_of_ship, trans_ship_flag,from_brnc_id,to_brnc_id,route_id
            FROM SYS_SHIP_DEST_MSTR
           WHERE ORG_BRNC_ID     = orgn_brnc
             AND DESC_BRNC_ID    = dest_brnc
             AND ROUTE_PREF_FLAG = lv_pref
             AND SHIP_SEQ        = lv_seq
      ORDER BY TRANS_SHIP_FLAG DESC;
    FLAG NUMBER := 0; --TO CHECK RECORDS EXIST OR NOT
    LN_MSGNUM NUMBER;
BEGIN
  FOR I IN C1(1,1) LOOP
      IF I.TRANS_SHIP_FLAG='T' THEN
          SHIP_SEQN := 1;
          NO_SHIP   := I.NO_OF_SHIP;
          CUR_LOC   := I.FROM_BRNC_ID;
          CUR_DEST     := I.TO_BRNC_ID;
          LC_ROUT   := I.ROUTE_ID;
          FLAG := 1;
          EXIT;
      ELSE
          SHIP_SEQN := 1;
          NO_SHIP   := I.NO_OF_SHIP;
          CUR_LOC   := I.FROM_BRNC_ID;
          CUR_DEST     := I.TO_BRNC_ID;
          LC_ROUT   := I.ROUTE_ID;
          FLAG := 1;
          EXIT;
      END IF;
  END LOOP;
END;
-----------------------------------------
/* For Web Booking */
/* This function will return whether Insurance is provided for the Client or Not -- Created By Venkat 18/04/2003 */
/* Modified by B.Emerson - trunc(sysdate) passed as an out parameter and out parameter expiry date is truncated and passes*/
PROCEDURE PRO_CLNT_INSU_FLAG(CLNT_ID VARCHAR2,INS_FLAG OUT VARCHAR2,PLCY_NO OUT VARCHAR2,EXPY_DATE OUT DATE, SYS_DATE OUT DATE)IS
  CURSOR CUR_CLNT_INSU(LV_CLNT_ID VARCHAR2) IS
  SELECT NVL(CM_REQ_INSU_FLAG,'N'),CM_INSU_PLCY_NO,CM_EXPY_DATE
    FROM SYS_CLNT_MSTR
   WHERE CM_CLNT_ID=LV_CLNT_ID;
BEGIN
    OPEN CUR_CLNT_INSU(CLNT_ID);
    FETCH CUR_CLNT_INSU INTO INS_FLAG,PLCY_NO,EXPY_DATE;
    IF CUR_CLNT_INSU%NOTFOUND THEN
        INS_FLAG := 'N';
        PLCY_NO := '';
        EXPY_DATE := '';
    END IF;
    CLOSE CUR_CLNT_INSU;
    IF EXPY_DATE IS NOT NULL THEN
        EXPY_DATE := TRUNC(EXPY_DATE);
    END IF;
    SYS_DATE := TRUNC(SYSDATE);
END;
-----------------------------------------
/* For Web Booking */
/* This procedure is used to Fetch the Insurance Service Information based on then
/* Insurance Type, Client  - Created by Venkat 18/04/2003 */
PROCEDURE PRO_SRVC_INFO (ins_type VARCHAR2,ins_amnt NUMBER,clnt_id VARCHAR2,ins_info OUT VARCHAR2) IS
     CURSOR CUR_GET_INV_PE(LV_INS_TYPE VARCHAR2,LV_REF_SRVC VARCHAR2,LV_INFO VARCHAR2,
                           LV_PRNT VARCHAR2,LV_SEQ NUMBER) IS
       SELECT SI_DEFA_TEXT
         FROM SYS_SRVC_INFO_REQM_MSTR
        WHERE SI_SRVC_ID      = LV_INS_TYPE
          AND SI_REFR_SRVC_ID = LV_REF_SRVC
          AND SI_INFO_OR_REQM = LV_INFO
          AND SI_PRNT_IN_GUIA = LV_PRNT
          AND SI_SEQ_NO       = LV_SEQ;

      CURSOR CUR_GET_INV_DETL(LV_CLNT_ID VARCHAR2) IS
        SELECT CM_INSU_TEXT,CM_INSU_PLCY_NO,CM_EXPY_DATE
          FROM SYS_CLNT_MSTR
         WHERE CM_CLNT_ID = LV_CLNT_ID;

      CURSOR CUR_GET_INV_PE1(LV_INS_TYPE VARCHAR2,LV_REF_SRVC VARCHAR2,LV_INFO VARCHAR2,
                                LV_PRNT VARCHAR2,LV_SEQ NUMBER) IS
        SELECT SI_DEFA_TEXT
          FROM SYS_SRVC_INFO_REQM_MSTR
         WHERE SI_SRVC_ID       = LV_INS_TYPE
           AND  SI_REFR_SRVC_ID = LV_REF_SRVC
           AND SI_INFO_OR_REQM  = LV_INFO
           AND  SI_PRNT_IN_GUIA = LV_PRNT
           AND SI_SEQ_NO        = LV_SEQ;

     LV_INSU_TEXT  VARCHAR2(100);
     LV_INSU_PLCY_NO VARCHAR2(30);
     LV_EXPY_DATE    DATE;
     CLNT_IDS VARCHAR2(15);
BEGIN
         IF INS_AMNT IS NULL AND INS_TYPE = 'INV-CX' THEN
                BEGIN
                    OPEN CUR_GET_INV_DETL(clnt_id); -- B. Emerson 11-Oct-2006
                    FETCH CUR_GET_INV_DETL INTO LV_INSU_TEXT,LV_INSU_PLCY_NO,LV_EXPY_DATE;
                    IF CUR_GET_INV_DETL%NOTFOUND THEN
                        INS_INFO := ' ';
                    END IF;
                    CLOSE CUR_GET_INV_DETL;
                    EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                    END;
            END IF;
            IF INS_AMNT >= 1000 OR INS_AMNT IS NULL THEN
                BEGIN
                    OPEN CUR_GET_INV_PE(ins_type,'INV','I','Y',1);
                    FETCH CUR_GET_INV_PE INTO INS_INFO;
                    IF CUR_GET_INV_PE%NOTFOUND THEN
                         INS_INFO := ' ';
                    END IF;
                    CLOSE CUR_GET_INV_PE;
                    EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                    END;
            ELSE
                BEGIN
                    OPEN CUR_GET_INV_PE1(ins_type,'INV','I','Y',2);
                    FETCH CUR_GET_INV_PE1 INTO INS_INFO;
                    IF CUR_GET_INV_PE1%NOTFOUND THEN
                         INS_INFO := ' ';
                    END IF;
                    CLOSE CUR_GET_INV_PE1;
                    EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                    END;
            END IF;
            IF INS_TYPE = 'INV-CX' THEN
                 PRO_GET_INV_MESG(INS_INFO,LV_INSU_TEXT,LV_INSU_PLCY_NO,LV_EXPY_DATE);
            ELSE
                 PRO_GET_INV_MESG(INS_INFO,INS_AMNT,' ',' ');
            END IF;

END;
------------------------------------------
PROCEDURE PRO_COD_TEXT (cod_srvc_amt NUMBER, cod_text OUT VARCHAR2)IS
     CURSOR CUR_GET_INV_PE(LV_SRVC_ID VARCHAR2,LV_REF_SRVC_ID VARCHAR2,
                           LV_INFO VARCHAR2,LV_PRNT VARCHAR2) IS
     SELECT SI_DEFA_TEXT
       FROM SYS_SRVC_INFO_REQM_MSTR
      WHERE SI_SRVC_ID      = LV_SRVC_ID
        AND SI_REFR_SRVC_ID = LV_REF_SRVC_ID
        AND SI_INFO_OR_REQM = LV_INFO
        AND SI_PRNT_IN_GUIA = LV_PRNT;

     LV_INSU_TEXT  VARCHAR2(100);
     LV_INSU_PLCY_NO VARCHAR2(30);
     LV_EXPY_DATE    DATE;
     CLNT_IDS VARCHAR2(15);
BEGIN
        OPEN CUR_GET_INV_PE('COD-1','COD','I','Y');
        FETCH CUR_GET_INV_PE INTO COD_TEXT;
        IF CUR_GET_INV_PE%NOTFOUND THEN
             COD_TEXT := ' ';
        END IF;
        CLOSE CUR_GET_INV_PE;
        /* Call procedure for COD TEXT */
    PRO_GET_INV_MESG(cod_text,COD_SRVC_AMT,' ',' ');
    END;
------------------------------------------------------------
    FUNCTION fun_slmn_id(client_id VARCHAR2) RETURN VARCHAR2 IS
        slmn_id VARCHAR2(15);
        CURSOR cur_slmn_id(lv_clnt_id VARCHAR2) IS SELECT cm_slmn_id FROM SYS_CLNT_MSTR WHERE
        cm_clnt_id = lv_clnt_id;
    BEGIN
      OPEN cur_slmn_id(client_id);
      FETCH cur_slmn_id INTO slmn_id;
      IF cur_slmn_id%NOTFOUND THEN
          slmn_id := NULL;
      END IF;
      CLOSE cur_slmn_id;
        RETURN(slmn_id);
    END;
------------------------------------------------------------
PROCEDURE PRO_SRVC_NAME(SRVC_ID VARCHAR2,SRVC VARCHAR2, SRVC_NAME OUT VARCHAR2) IS
  CURSOR CUR_SRVC_NAME IS
    SELECT SM_SRVC_NAME
      FROM SYS_SRVC_MSTR
     WHERE SM_SRVC_ID      = SRVC_ID
       AND SM_REFR_SRVC_ID = SRVC;
BEGIN
  OPEN CUR_SRVC_NAME;
  FETCH CUR_SRVC_NAME INTO SRVC_NAME;
  IF CUR_SRVC_NAME%NOTFOUND THEN
      SRVC_NAME := NULL;
  END IF;
  CLOSE CUR_SRVC_NAME;
END;
--------------------------------------------------------------
FUNCTION fun_chk_actv_trif (orgn_brnc VARCHAR2, dest_brnc VARCHAR2) RETURN NUMBER IS
    CURSOR cur_actv_trif (mdul_id VARCHAR2, parm_type VARCHAR2, code1 VARCHAR2, vlue1 NUMBER)IS
        SELECT COUNT(*)
            FROM SYS_PARM_MSTR
            WHERE pm_mdul_id = mdul_id
            AND pm_parm_type = parm_type
            AND pm_parm_code1 = code1
            AND pm_vlue1_id = vlue1;
    CURSOR cur_trif_dist IS
        SELECT bd_dstn
            FROM    SYS_BRNC_DSTN
            WHERE substr(bd_orgn_brnc,1,3)= substr(orgn_brnc,1,3)
            AND  substr(bd_dest_brnc,1,3) = substr(dest_brnc,1,3);
    ln_count NUMBER:=0;
    lc_dstn  SYS_BRNC_DSTN.bd_dstn%TYPE:=0;
    ln_mesg  NUMBER;
BEGIN
    OPEN cur_actv_trif('BOK','TARIF_TYPE','KILOMETER',1);
    FETCH cur_actv_trif INTO ln_count;
    CLOSE cur_actv_trif;
    IF NVL(ln_count,0)>0 THEN
        OPEN cur_trif_dist;
        FETCH cur_trif_dist INTO lc_dstn;
        CLOSE cur_trif_dist;
        IF NVL(lc_dstn,0) > 0 THEN
            RETURN(lc_dstn);
        END IF;
    END IF;
    RETURN(NULL);
END;
------------------



/* cater to performance issue in web booking, function process is modified by Amaladoss on 23/07/2008 */

FUNCTION FUN_CLNT_CRDT_AMT(ARG_CLNT_ID_MN in VARCHAR2) RETURN NUMBER IS
  CURSOR cur_clnt_grp_list (cur_grp_id in varchar2) is
    select cm_clnt_id FROM SYS_CLNT_MSTR
       WHERE cm_grup_clnt_id =cur_grp_id;

  CURSOR cur_clnt_cr_lmt (ARG_CLNT_ID in varchar2) is
        select  cm_grup_clnt_id, CM_CRED_LIMT,cm_clnt_type
             from sys_clnt_mstr
         where cm_clnt_id=arg_clnt_id;

  CURSOR cur_grp_clnt_cr_lmt (ARG_GRP_CLNT_ID in varchar2) is
     select  CM_CRED_LIMT
             FROM SYS_CLNT_MSTR
    WHERE  CM_CLNT_ID = ARG_GRP_CLNT_ID;

   LN_TOT_PEND_AMT NUMBER :=0;
   LN_TOT_PEND_CUM_AMT NUMBER :=0;
   LC_CRED_LIMT NUMBER :=0;
   lc_grup_clnt_id  sys_clnt_mstr.CM_CLNT_ID%type;
   LC_clnt_type     sys_clnt_mstr.CM_CLNT_TYPE%type;
   v_err varchar2(300);

BEGIN
 for cur_clmt in cur_clnt_cr_lmt(ARG_CLNT_ID_MN)
 loop
    lc_grup_clnt_id := cur_clmt.cm_grup_clnt_id;
    LC_CRED_LIMT := cur_clmt.CM_CRED_LIMT;
    lc_clnt_type := cur_clmt.cm_clnt_type;
 end loop;

  if lc_grup_clnt_id is NULL or lc_clnt_type in ('I','C') then
     LN_TOT_PEND_AMT := FUN_CLNT_CRDT_AMT_ADN(ARG_CLNT_ID_MN);
  else
     /*for cur1 in cur_clnt_grp_list(lc_grup_clnt_id)
     loop
    LN_TOT_PEND_CUM_AMT := FUN_CLNT_CRDT_AMT_ADN(cur1.cm_clnt_id);
        LN_TOT_PEND_AMT :=LN_TOT_PEND_AMT + NVL(LN_TOT_PEND_CUM_AMT,0);
     end loop;
    */
ln_tot_pend_amt:=FUN_CLNT_CRDT_AMT_ADN_GRUP(lc_grup_clnt_id);
     for cur_grplmt in cur_grp_clnt_cr_lmt(LC_GRUP_CLNT_ID)
     loop
         LC_CRED_LIMT := cur_grplmt.CM_CRED_LIMT;
     end loop;

  end if;
  return (LC_CRED_LIMT - ln_tot_pend_amt );
END;


-------------------------


FUNCTION FUN_CLNT_CRDT_AMT_ADN(ARG_CLNT_ID_MN VARCHAR2) RETURN NUMBER IS
   LN_PAID_AMT       NUMBER :=0;
   LN_TOT_DR_AMT     NUMBER :=0;
   LN_TOT_CR_AMT     NUMBER :=0;
   LN_DRVD_AMT       NUMBER :=0;
   LN_TOT_ADL_DR_AMT     NUMBER :=0;
   LN_WEB_BOK_AMT     NUMBER :=0;
   LN_NET_AMT        NUMBER :=0;
   LD_CUT_OFF_DT     DATE;
   V_ERR             VARCHAR2(300);
   LN_BGD_GH_AMT    NUMBER:=0;
   LN_BGD_INV_CAN_AMT NUMBER:=0;
   CURSOR cur_cut_of_date (arg_clnt_id varchar2) IS
        SELECT NVL(ACP_PEND_AMT,0) pend_amt,
        trunc(NVL(ACP_UPDTD_DATE,ACP_PRCSD_DATE)) cut_date
          FROM   ADM_CLNT_PEND_AMT
    WHERE   ACP_CLNT_ID = arg_clnt_id;

   CURSOR cur_dr_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,LC_H in varchar2,
            LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_A in varchar2,LC_C in varchar2,
              arg_clnt_id in varchar2,arg_cut_off_dt in date) IS
    SELECT  SUM(NVL(gh_guia_amnt,0))  dr_amt
    FROM    BOK_GUIA_HEAD
    where   gh_bill_clnt_id = arg_clnt_id
    and     trunc(GH_ISSE_DATE) > trunc(arg_cut_off_dt)  /*Code Added by emerson on 06-dec-2006*/
    AND     gh_paid_date IS NULL
    AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND     gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H)
    AND     gh_docu_type IN (LC_G,LC_D,LC_Q)
    AND     gh_actv_flag =  LC_A
    AND     gh_pymt_type =  LC_C
    AND     gh_used_date IS NULL;

   CURSOR cur_cr_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,
                       LC_H in varchar2,LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_A in varchar2,
              arg_clnt_id in varchar2, arg_cut_off_dt in date)  IS
    SELECT SUM(NVL(gh_guia_amnt,0)) cr_amt
    FROM   BOK_GUIA_HEAD
    where  gh_bill_clnt_id = arg_clnt_id
        and    trunc(GH_ISSE_DATE) <= trunc(arg_cut_off_dt) /*Code Added by emerson on 06-dec-2006*/
        and    gh_paid_date > trunc(arg_CUT_OFF_DT + 1) /*Code Added by emerson on 06-dec-2006*/
    AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND    gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H)
    AND    gh_docu_type IN  (LC_G,LC_D,LC_Q)
    AND    gh_actv_flag =  LC_A
    AND    gh_used_date IS NULL;

   CURSOR cur_dr_adl_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,
            LC_H in varchar2,LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_I in varchar2,
                        LC_C in varchar2,arg_clnt_id in varchar2,arg_cut_off_dt in date) IS
    SELECT SUM(NVL(gh_guia_amnt,0))  adl_dr_amt
    FROM  BOK_GUIA_HEAD
    where gh_bill_clnt_id = arg_clnt_id
    and   trunc(GH_ISSE_DATE) <= trunc(arg_cut_off_dt) /*Code Added by emerson on 06-dec-2006*/
    AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND   gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H)
    AND   gh_docu_type IN  (LC_GA,LC_D,LC_Q)
    AND   gh_actv_flag IN (LC_I,LC_C)
    AND   gh_used_date IS NULL
    AND   MDFD_ON > TRUNC(ARG_CUT_OFF_DT + 1); /*Code Added by emerson on 06-dec-2006*/

   CURSOR cur_pend_amt (arg_clnt_id in varchar2) IS
          SELECT SUM(NVL(ACP_PEND_AMT,0)) pend_amt
          FROM   ADM_CLNT_PEND_AMT
      WHERE  ACP_CLNT_ID= ARG_CLNT_ID;



  GUIA_AMT NUMBER:=0;
  SUM_BGD_GH_AMT NUMBER:=0;

BEGIN

  FOR recdt in cur_cut_of_date (ARG_CLNT_ID_MN)
  loop
    LD_CUT_OFF_DT :=  recdt.cut_date;

     LN_DRVD_AMT := recdt.pend_amt;
  end loop;

  FOR recdr in cur_dr_calc('IV','P','N','S','G','T','H','G','D','Q','A','C',ARG_CLNT_ID_MN, LD_CUT_OFF_DT)
  loop
     LN_TOT_DR_AMT := recdr.dr_amt;
  end loop;


  FOR reccr in cur_cr_calc ( 'IV','P','N','S','G','T','H','G','D','Q','A',ARG_CLNT_ID_MN, LD_CUT_OFF_DT )
  loop
         LN_TOT_CR_AMT := reccr.cr_amt;
  end loop;

  FOR recadl in cur_dr_adl_calc ( 'IV','P','N','S','G','T','H','G','D','Q','I','C',ARG_CLNT_ID_MN, LD_CUT_OFF_DT )
  loop
      LN_TOT_ADL_DR_AMT := recadl.adl_dr_amt;
  end loop;


 LN_NET_AMT :=  (NVL(LN_DRVD_AMT,0) + NVL(ln_tot_Dr_amt,0) - NVL(LN_TOT_ADL_DR_AMT,0)) - NVL(ln_tot_Cr_amt,0)  ;

 RETURN( LN_NET_AMT );

END;


----------------------


 FUNCTION FUN_CLNT_CRDT_AMT_ADN_GRUP(ARG_CLNT_ID_GRUP VARCHAR2) RETURN NUMBER IS
   LN_PAID_AMT       NUMBER :=0;
   LN_TOT_DR_AMT     NUMBER :=0;
   LN_TOT_CR_AMT     NUMBER :=0;
   LN_DRVD_AMT       NUMBER :=0;
   LN_TOT_ADL_DR_AMT     NUMBER :=0;
   LN_WEB_BOK_AMT     NUMBER :=0;
   LN_NET_AMT        NUMBER :=0;
   LD_CUT_OFF_DT     DATE;
   V_ERR             VARCHAR2(300);
   LN_BGD_GH_AMT    NUMBER:=0;
   LN_BGD_INV_CAN_AMT NUMBER:=0;
   CURSOR cur_cut_of_date IS
        SELECT /*+ INDEX(ADM_CLNT_PEND_AMT SYS_C003026) */ sum(NVL(ACP_PEND_AMT,0)) pend_amt
          FROM   ADM_CLNT_PEND_AMT
    WHERE   ACP_CLNT_ID IN (SELECT CM_clnt_id from sys_clnt_mstr where cm_grup_clnt_id=ARG_CLNT_ID_GRUP);

  CURSOR cur_cut_of_maxdate IS
        SELECT /*+ INDEX(ADM_CLNT_PEND_AMT SYS_C003026) */MAX(ACP_UPDTD_DATE) cut_date
          FROM   ADM_CLNT_PEND_AMT
    WHERE   ACP_CLNT_ID IN (SELECT CM_clnt_id from sys_clnt_mstr where cm_grup_clnt_id=ARG_CLNT_ID_GRUP);

   CURSOR cur_dr_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,LC_H in varchar2,
            LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_A in varchar2,LC_C in varchar2,
              arg_cut_off_dt in date) IS
    SELECT  SUM(NVL(gh_guia_amnt,0))  dr_amt
    FROM    BOK_GUIA_HEAD
    where   EXISTS (SELECT null from sys_clnt_mstr
            where CM_clnt_id = gh_bill_clnt_id
                AND  cm_grup_clnt_id=ARG_CLNT_ID_GRUP)
    and     trunc(GH_ISSE_DATE) > trunc(arg_cut_off_dt)  /*Code Added by emerson on 06-dec-2006*/
    AND     gh_paid_date IS NULL
    AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND     gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H,LC_IV)
    AND     gh_docu_type IN (LC_G,LC_D,LC_Q,LC_P)
    AND     gh_actv_flag =  LC_A
    AND     gh_pymt_type =  LC_C
    AND     gh_used_date IS NULL;

   CURSOR cur_cr_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,
                       LC_H in varchar2,LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_A in varchar2,
              arg_cut_off_dt in date)  IS
    SELECT SUM(NVL(gh_guia_amnt,0)) cr_amt
    FROM   BOK_GUIA_HEAD
    where  EXISTS (SELECT null from sys_clnt_mstr
             where CM_clnt_id = gh_bill_clnt_id
                 and cm_grup_clnt_id=ARG_CLNT_ID_GRUP)
        and    trunc(GH_ISSE_DATE) <= trunc(arg_cut_off_dt) /*Code Added by emerson on 06-dec-2006*/
        and    gh_paid_date > trunc(arg_CUT_OFF_DT) /*Code Added by emerson on 06-dec-2006*/
         AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND    gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H,LC_IV)
    AND    gh_docu_type IN  (LC_G,LC_D,LC_Q,LC_P)
    AND    gh_actv_flag =  LC_A
    AND    gh_used_date IS NULL;

   CURSOR cur_dr_adl_calc (LC_IV in varchar2,LC_P in varchar2,LC_N in varchar2,LC_S in varchar2,LC_G in varchar2,LC_T in varchar2,
            LC_H in varchar2,LC_GA in varchar2,LC_D in varchar2,LC_Q in varchar2,LC_I in varchar2,
                        LC_C in varchar2,arg_cut_off_dt in date) IS
    SELECT SUM(NVL(gh_guia_amnt,0))  adl_dr_amt
    FROM  BOK_GUIA_HEAD
    where EXISTS (SELECT null from sys_clnt_mstr
          where CM_clnt_id = gh_bill_clnt_id
              and cm_grup_clnt_id=ARG_CLNT_ID_GRUP)
    and   trunc(GH_ISSE_DATE) <= trunc(arg_cut_off_dt) /*Code Added by emerson on 06-dec-2006*/
     AND   DECODE (GH_DOCU_TYPE,LC_P,DECODE(GH_GUIA_TYPE,LC_N,0,1),1) = 1
    AND   gh_guia_type IN (LC_N,LC_S,LC_G,LC_T,LC_H,LC_IV)
    AND   gh_docu_type IN  (LC_GA,LC_D,LC_Q,LC_P)
    AND   gh_actv_flag IN (LC_I,LC_C)
    AND   gh_used_date IS NULL
    AND   MDFD_ON > TRUNC(ARG_CUT_OFF_DT + 1); /*Code Added by emerson on 06-dec-2006*/

   CURSOR cur_pend_amt  IS
          SELECT SUM(NVL(ACP_PEND_AMT,0)) pend_amt
          FROM   ADM_CLNT_PEND_AMT
      WHERE  EXISTS (SELECT null from sys_clnt_mstr
               where CM_clnt_id = ACP_CLNT_ID
               AND  cm_grup_clnt_id=ARG_CLNT_ID_GRUP);



  GUIA_AMT NUMBER:=0;
  SUM_BGD_GH_AMT NUMBER:=0;

BEGIN

  FOR recdt in cur_cut_of_date
  loop

     LN_DRVD_AMT := recdt.pend_amt;
  end loop;

  FOR max_recdt in cur_cut_of_maxdate
  loop

    LD_CUT_OFF_DT := max_recdt.cut_date;
  end loop;


  FOR recdr in cur_dr_calc('IV','P','N','S','G','T','H','G','D','Q','A','C', LD_CUT_OFF_DT)
  loop
     LN_TOT_DR_AMT := recdr.dr_amt;
  end loop;


  FOR reccr in cur_cr_calc ( 'IV','P','N','S','G','T','H','G','D','Q','A', LD_CUT_OFF_DT )
  loop
         LN_TOT_CR_AMT := reccr.cr_amt;
  end loop;

  FOR recadl in cur_dr_adl_calc ( 'IV','P','N','S','G','T','H','G','D','Q','I','C', LD_CUT_OFF_DT )
  loop
      LN_TOT_ADL_DR_AMT := recadl.adl_dr_amt;
  end loop;


 LN_NET_AMT :=  (NVL(LN_DRVD_AMT,0) + NVL(ln_tot_Dr_amt,0) - NVL(LN_TOT_ADL_DR_AMT,0)) - NVL(ln_tot_Cr_amt,0);

 RETURN( LN_NET_AMT );

END;





-----------------------------

/**************************** commented for performance issue in Web booking.
    FUNCTION FUN_CLNT_CRDT_AMT(client_id VARCHAR2) RETURN NUMBER IS

        --Cursor for fetching client type and status of client
        CURSOR cur_getactiveclient(lc_client_id IN VARCHAR2)    IS
                SELECT cm_clnt_id, cm_cred_limt, cm_grup_clnt_id, cm_clnt_type
                  FROM SYS_CLNT_MSTR
               WHERE cm_clnt_id = lc_client_id;


        -- Cursor to sum values for client from guia head
        CURSOR cur_sum_clnt(lc_client_id IN VARCHAR2,
            lc_c IN VARCHAR2, lc_n IN VARCHAR2,lc_s IN VARCHAR2,lc_g IN VARCHAR2,
            lc_d IN VARCHAR2,    lc_a IN VARCHAR2,lc_t IN VARCHAR2,lc_q IN VARCHAR2,lc_h IN VARCHAR2) IS
                    SELECT SUM(NVL(gh_guia_amnt,0))
                        FROM BOK_GUIA_HEAD
                     WHERE gh_bill_clnt_id = lc_client_id AND
                                gh_pymt_type = lc_c AND
                                gh_paid_date IS NULL AND
                                gh_guia_type IN (lc_n,lc_s,lc_g,lc_t,lc_h) AND
                                gh_docu_type IN (lc_g,lc_d,lc_q) AND
                                gh_used_date IS NULL AND
                                gh_actv_flag = lc_a;

        CURSOR cur_sum_grup_clnt(lc_grup_client_id IN VARCHAR2,
            lc_c IN VARCHAR2, lc_n IN VARCHAR2,lc_s IN VARCHAR2,lc_g IN VARCHAR2,
            lc_d IN VARCHAR2, lc_a IN VARCHAR2,lc_t IN VARCHAR2,lc_q IN VARCHAR2,lc_h IN VARCHAR2) IS
                SELECT SUM(NVL(gh_guia_amnt,0))
                FROM BOK_GUIA_HEAD a,  sys_clnt_mstr b
                where a.gh_bill_clnt_id = b.CM_CLNT_ID
                and b.cm_grup_clnt_id = lc_grup_client_id
                 and gh_bill_brnc_id = gh_bill_brnc_id
                         AND gh_paid_date IS NULL
                         AND    gh_guia_type IN (lc_n,lc_s,lc_g,lc_t,lc_h)--lc_h added by Emerson on 04/02/2005
                         AND gh_docu_type IN  (lc_g,lc_d,lc_q)
                        AND gh_actv_flag =  lc_a
                        AND    gh_pymt_type = lc_c
                        AND gh_used_date IS NULL;


                 ---- old query
                SELECT SUM(NVL(gh_guia_amnt,0))
                        FROM BOK_GUIA_HEAD
                     WHERE gh_bill_clnt_id IN (SELECT
                                 cm_clnt_id FROM SYS_CLNT_MSTR
                                 WHERE cm_grup_clnt_id = lc_grup_client_id)
                         AND    gh_pymt_type = lc_c
                         AND gh_paid_date IS NULL
                         AND    gh_guia_type IN (lc_n,lc_s,lc_g,lc_t,lc_h)--lc_h added by Emerson on 04/02/2005
                         AND gh_docu_type IN (lc_g,lc_d,lc_q)
                         AND gh_used_date IS NULL
                         AND gh_actv_flag = lc_a;


        unpd_vlue NUMBER := 0;
        credit_limit_var NUMBER := 0;
        clnt_id SYS_CLNT_MSTR.CM_CLNT_ID%TYPE;
        grup_clnt_id SYS_CLNT_MSTR.CM_GRUP_CLNT_ID%TYPE;
        clnt_type SYS_CLNT_MSTR.CM_CLNT_TYPE%TYPE;

    BEGIN
        OPEN cur_getactiveclient(client_id);
        FETCH cur_getactiveclient INTO clnt_id, credit_limit_var, grup_clnt_id, clnt_type;
        IF cur_getactiveclient%NOTFOUND THEN
            CLOSE cur_getactiveclient;
            RETURN(0);
        END IF;
        CLOSE cur_getactiveclient;
        IF clnt_type IN ('I','C') THEN
            OPEN cur_sum_clnt(client_id,'C','N','S','G','D','A','T','Q','H');
            FETCH cur_sum_clnt INTO unpd_vlue;
            CLOSE cur_sum_clnt;

            RETURN(NVL(credit_limit_Var,0) - NVL(unpd_vlue,0));

        ELSIF clnt_type IN ('G','N') THEN

                --Fetch the value credit limit of group head client
                OPEN cur_getactiveclient(grup_clnt_id);
                FETCH cur_getactiveclient INTO clnt_id, credit_limit_var, grup_clnt_id, clnt_type;
                IF cur_getactiveclient%NOTFOUND THEN
                    CLOSE cur_getactiveclient;
                    RETURN(0);
                END IF;
                CLOSE cur_getactiveclient;

                -- To sum up values for all clients under the Group client in the FTT
                OPEN cur_sum_grup_clnt(clnt_id,'C','N','S','G','D','A','T','Q','H');
                FETCH cur_sum_grup_clnt INTO unpd_vlue;
                CLOSE cur_sum_grup_clnt;

                RETURN(NVL(credit_limit_Var,0) - NVL(unpd_vlue,0));

        END IF;
    END;

*****************************************************/

------------------
    FUNCTION fun_chck_grup_clnt(CLNT_ID IN VARCHAR2) RETURN VARCHAR2 IS
        lv_clnt_id SYS_CLNT_MSTR.CM_CLNT_ID%TYPE;
        lv_grup_id SYS_CLNT_MSTR.CM_GRUP_CLNT_ID%TYPE;
        CURSOR cur_grup_clnt(lv1_clnt_id VARCHAR2) IS
        SELECT cm_clnt_id,cm_grup_clnt_id FROM SYS_CLNT_MSTR
        WHERE cm_clnt_id = lv1_clnt_id;
    BEGIN
        OPEN cur_grup_clnt(clnt_id);
        FETCH cur_grup_clnt INTO lv_clnt_id,lv_grup_id;
        IF cur_grup_clnt%NOTFOUND THEN
            lv_clnt_id := '';
            lv_grup_id := '';
        END IF;
        CLOSE cur_grup_clnt;

        IF LV_GRUP_ID IS NULL THEN
            RETURN LV_CLNT_ID;
        ELSE
            RETURN LV_GRUP_ID;
        END IF;
    END;
------------------
PROCEDURE pro_ftch_srvs_covd(trif_type VARCHAR2, ead OUT VARCHAR2, ack OUT VARCHAR2,
    cod OUT VARCHAR2, inv OUT VARCHAR2) IS
    CURSOR cur_ftch_srvc IS
        SELECT PM_PARM_CODE1
            FROM SYS_PARM_MSTR
            WHERE PM_MDUL_ID='WEB' AND
                PM_PARM_TYPE=trif_type;
    lc_srvc      SYS_PARM_MSTR.PM_PARM_CODE1%TYPE;
BEGIN
        OPEN cur_ftch_srvc;
        LOOP
            FETCH cur_ftch_srvc INTO lc_srvc;
            IF cur_ftch_srvc%NOTFOUND THEN
                EXIT;
            END IF;
            IF lc_srvc NOT IN ('PACKETS', 'ENVELOPES', 'RAD') THEN
                IF lc_srvc = 'EAD' THEN
                    ead:=lc_srvc;
                ELSIF lc_srvc = 'ACK' THEN
                    ack:=lc_srvc;
                ELSIF lc_srvc = 'COD' THEN
                    cod:=lc_srvc;
                ELSIF lc_srvc = 'INV' THEN
                    inv:=lc_srvc;
                END IF;
            END IF;
        END LOOP;
        CLOSE cur_ftch_srvc;
END;
------------------
FUNCTION fun_ftch_trif_exist(clnt_id VARCHAR2, orgn_brnc VARCHAR2, dest_brnc VARCHAR2,
    trif_type VARCHAR2)
    RETURN VARCHAR2 IS
    CURSOR cur_ftch_trif_exist IS
    SELECT WT_TRIF_SLAB
        FROM WEB_CLNT_SRVC_TRIF
        WHERE WT_ORGN_CLNT_ID=clnt_id    AND
            substr(WT_ORGN_BRNC_ID,1,3)=substr(orgn_brnc,1,3) AND
            substr(WT_DEST_BRNC_ID,1,3)=substr(dest_brnc,1,3) AND
            WT_TRIF_SLAB   =trif_type;
    lc_TRIF_SLAB WEB_CLNT_SRVC_TRIF.WT_TRIF_SLAB%TYPE;
BEGIN
    OPEN cur_ftch_trif_exist;
    FETCH cur_ftch_trif_exist INTO lc_TRIF_SLAB;
    IF(cur_ftch_trif_exist%NOTFOUND) THEN
    RETURN 'N';
    ELSE
    RETURN 'Y';
    END IF;
    CLOSE cur_ftch_trif_exist;

    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB'||lc_TRIF_SLAB);
    --IF lc_TRIF_SLAB IS NOT NULL THEN
    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB11111');
        --RETURN ('Y');
    --ELSE
    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB1NNNNNNNNNNNNN');
        --RETURN ('N');
    --END IF;
END;
------------------
FUNCTION fun_ftch_trif_exist_km(clnt_id VARCHAR2, km_dist NUMBER, trif_type VARCHAR2)--AAP05
    RETURN VARCHAR2 IS
    CURSOR cur_ftch_trif_exist IS
    SELECT WT_TRIF_SLAB
        FROM WEB_CLNT_SRVC_TRIF_KM
        WHERE WT_ORGN_CLNT_ID = clnt_id
        AND km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM
        AND WT_TRIF_SLAB = trif_type;
    lc_TRIF_SLAB WEB_CLNT_SRVC_TRIF.WT_TRIF_SLAB%TYPE;
BEGIN
    OPEN cur_ftch_trif_exist;
    FETCH cur_ftch_trif_exist INTO lc_TRIF_SLAB;
    IF(cur_ftch_trif_exist%NOTFOUND) THEN
    RETURN 'N';
    ELSE
    RETURN 'Y';
    END IF;
    CLOSE cur_ftch_trif_exist;

    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB'||lc_TRIF_SLAB);
    --IF lc_TRIF_SLAB IS NOT NULL THEN
    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB11111');
        --RETURN ('Y');
    --ELSE
    --DBMS_OUTPUT.PUT_LINE('lc_TRIF_SLAB1NNNNNNNNNNNNN');
        --RETURN ('N');
    --END IF;
END;
FUNCTION fun_ftch_trif_amnt(clnt_id VARCHAR2, orgn_brnc VARCHAR2, dest_brnc VARCHAR2,
    trif_type VARCHAR2, refr_serv_id VARCHAR2, serv_id VARCHAR2)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT WT_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF
        WHERE WT_ORGN_CLNT_ID=clnt_id     AND
            substr(WT_ORGN_BRNC_ID,1,3)=substr(orgn_brnc,1,3)      AND
            substr(WT_DEST_BRNC_ID,1,3)=substr(dest_brnc,1,3)      AND
            WT_TRIF_SLAB   =trif_type    AND
            WT_REFR_SRVC_ID=refr_serv_id AND
            WT_SRVC_ID     =serv_id AND WT_TRIF_AMNT <> 0;
    ln_amnt NUMBER;
BEGIN
    OPEN cur_ftch_trif_amnt;
    FETCH cur_ftch_trif_amnt INTO ln_amnt;
    IF cur_ftch_trif_amnt%NOTFOUND THEN
        ln_amnt:=0;
    END IF;
    CLOSE cur_ftch_trif_amnt;
    RETURN(ln_amnt);
END;
FUNCTION fun_ftch_trif_amnt_KM(clnt_id VARCHAR2, orgn_brnc VARCHAR2, km_dist NUMBER,
    trif_type VARCHAR2, refr_serv_id VARCHAR2, serv_id VARCHAR2)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT WT_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF_KM
        WHERE WT_ORGN_CLNT_ID=clnt_id     AND
            km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND
            WT_TRIF_SLAB   =trif_type    AND
            WT_REFR_SRVC_ID=refr_serv_id AND
            WT_SRVC_ID     =serv_id AND WT_TRIF_AMNT <> 0;
    ln_amnt NUMBER;
BEGIN
    OPEN cur_ftch_trif_amnt;
    FETCH cur_ftch_trif_amnt INTO ln_amnt;
    IF cur_ftch_trif_amnt%NOTFOUND THEN
        ln_amnt:=0;
    END IF;
    CLOSE cur_ftch_trif_amnt;
    RETURN(ln_amnt);
END;
FUNCTION fun_ftch_trif_amnt_C(clnt_id VARCHAR2, orgn_brnc VARCHAR2,dest_brnc VARCHAR2,trif_slab VARCHAR2,
    refr_serv_id VARCHAR2, serv_id VARCHAR2,weight NUMBER,VOLUME NUMBER)RETURN NUMBER IS
    lc_weight VARCHAR2(80);
    LC_VOLUME VARCHAR2(80);
    lc_fact_type VARCHAR2(80);
    lc_ftch_non WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    lc_ftch_kg WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    lc_ftch_cum WEB_CLNT_SRVC_TRIF.WT_FACTOR_VALUE%TYPE;
    
    lc_ftch_kg_factor_value WEB_CLNT_SRVC_TRIF_FACTOR.WCP_FACTOR_VALUE%TYPE;
    lc_ftch_kg_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;
    lc_ftch_kg_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;
    aditional_weight NUMBER;
    
    lc_ftch_cum_factor_value WEB_CLNT_SRVC_TRIF_FACTOR.WCP_FACTOR_VALUE%TYPE;
    lc_ftch_cum_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;
    lc_ftch_cum_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT%TYPE;
    aditional_volume NUMBER;
    
    adiAmnt WEB_CLNT_SRVC_TRIF_FACTOR.WCP_TRIF_AMNT_EXED%TYPE := 0;
    flg NUMBER := 1;--FLAG PARA VALIDAR SI EXISTE REGISTRO EN TABLA FACTOR
    
    CURSOR CUR_FTCH_NON_WT_VOL(lc_fact_type VARCHAR2) IS
           SELECT WT_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF
            WHERE WT_ORGN_CLNT_ID = clnt_id         AND
                  substr(WT_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)    AND
                  substr(WT_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)    AND
                  WT_TRIF_SLAB    = trif_slab    AND
                  WT_REFR_SRVC_ID = refr_serv_id AND
                  WT_SRVC_ID      = serv_id      AND
                  WT_FACTOR          = lc_fact_type;
                  
     CURSOR CUR_FTCH_FACTOR_WT_VOL(lc_fact_type VARCHAR2) IS--AAP07
           SELECT WCP_TRIF_AMNT, WCP_FACTOR_VALUE, NVL(WCP_TRIF_AMNT_EXED,0)
             FROM WEB_CLNT_SRVC_TRIF_FACTOR
            WHERE WCP_ORGN_CLNT_ID = clnt_id         AND
                  substr(WCP_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)    AND
                  substr(WCP_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)    AND
                  WCP_TRIF_SLAB       = trif_slab    AND
                  WCP_REFR_SRVC_ID = refr_serv_id AND
                  WCP_SRVC_ID          = serv_id      AND
                  WCP_FACTOR           = lc_fact_type
            ORDER BY WCP_FACTOR_VALUE;
            
    CURSOR cur_ftch_trif_amnt IS
    SELECT WT_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF
        WHERE WT_ORGN_CLNT_ID=clnt_id AND
            substr(WT_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3) AND
            substr(WT_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3) AND
            WT_TRIF_SLAB = trif_slab AND
            WT_REFR_SRVC_ID = refr_serv_id AND
            WT_SRVC_ID = serv_id AND WT_TRIF_AMNT <> 0;
            
    CURSOR CUR_FTCH_FACTOR_VALID IS
     SELECT WCP_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF_FACTOR
            WHERE WCP_ORGN_CLNT_ID = clnt_id AND
                  substr(WCP_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3) AND
                  substr(WCP_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3) AND
                  WCP_TRIF_SLAB = trif_slab AND
                  WCP_REFR_SRVC_ID = refr_serv_id AND
                  WCP_SRVC_ID = serv_id AND   
                  ROWNUM <= 1
            ORDER BY WCP_FACTOR_VALUE;                        
    BEGIN
        lc_ftch_kg_trif_amnt_acum :=0;
        lc_ftch_cum_trif_amnt_acum :=0;
        lc_weight:=weight;
        lc_volume:=volume;
        
        OPEN CUR_FTCH_FACTOR_VALID;
        FETCH CUR_FTCH_FACTOR_VALID INTO lc_ftch_non;
        IF CUR_FTCH_FACTOR_VALID%NOTFOUND THEN
            flg:=0;
        END IF;
        CLOSE CUR_FTCH_FACTOR_VALID;
        
        IF flg=0 THEN
            
            OPEN cur_ftch_trif_amnt;
            FETCH cur_ftch_trif_amnt INTO lc_ftch_non;
            IF cur_ftch_trif_amnt%NOTFOUND THEN
                lc_ftch_non:=0;
            END IF;
            CLOSE cur_ftch_trif_amnt;
            RETURN (lc_ftch_non);
            
        ELSE
            OPEN CUR_FTCH_FACTOR_WT_VOL('KG');
            LOOP
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_kg_trif_amnt, lc_ftch_kg_factor_value, adiAmnt ;
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN
                    EXIT;
                END IF;
                lc_ftch_kg := adiAmnt;--almacena costo kilo adicional
                lc_ftch_kg_trif_amnt_acum:=lc_ftch_kg_trif_amnt_acum + lc_ftch_kg_trif_amnt;
                
                if lc_weight <= lc_ftch_kg_factor_value then
                    begin
                        aditional_weight := 0;
                        exit;
                    end;
                 else
                    begin
                        aditional_weight := lc_weight - lc_ftch_kg_factor_value;
                    end;
                 end if;                
             END LOOP;
            CLOSE CUR_FTCH_FACTOR_WT_VOL; 
                    
            --open CUR_FTCH_NON_WT_VOL('KG');
            /*open CUR_FTCH_NON_WT_VOL('NON');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_kg:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;*/
            IF lc_ftch_kg IS NULL THEN 
                lc_ftch_kg :=0; 
            END IF; 
            
            IF lc_ftch_kg_trif_amnt_acum IS NULL THEN 
                lc_ftch_kg_trif_amnt_acum :=0; 
            END IF; 
            
            IF lc_ftch_kg_trif_amnt_acum > 0 THEN 
                IF aditional_weight >0 THEN 
                    lc_ftch_kg_trif_amnt_acum := lc_ftch_kg_trif_amnt_acum + (aditional_weight*lc_ftch_kg); 
                END IF; 
            ELSE  
                lc_ftch_kg_trif_amnt_acum := lc_weight * lc_ftch_kg; 
            END IF; 
            
            adiAmnt :=0;--se reinicia valor de variable que mantiene costo adicional
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('CUM');
            LOOP
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_cum_trif_amnt, lc_ftch_cum_factor_value, adiAmnt;
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN
                    EXIT;
                END IF;
                lc_ftch_cum := adiAmnt;--almacena costo volumen adicional
                lc_ftch_cum_trif_amnt_acum:=lc_ftch_cum_trif_amnt_acum + lc_ftch_cum_trif_amnt;
                
                if lc_volume <= lc_ftch_cum_factor_value then
                    begin
                        aditional_volume := 0;
                        exit;
                    end;
                 else
                    begin
                        aditional_volume := lc_volume - lc_ftch_cum_factor_value;
                    end;
                 end if;                
             END LOOP;
            CLOSE CUR_FTCH_FACTOR_WT_VOL;
            
            /*open CUR_FTCH_NON_WT_VOL('CUM');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_cum:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;*/
            IF lc_ftch_cum IS NULL THEN 
                lc_ftch_cum :=0; 
            END IF;
            
             IF lc_ftch_cum_trif_amnt_acum IS NULL THEN 
                lc_ftch_cum_trif_amnt_acum :=0; 
            END IF; 
            
            IF lc_ftch_cum_trif_amnt_acum > 0 THEN 
                IF aditional_volume >0 THEN 
                    lc_ftch_cum_trif_amnt_acum := lc_ftch_cum_trif_amnt_acum + (aditional_volume*lc_ftch_cum); 
                END IF; 
            ELSE  
                lc_ftch_cum_trif_amnt_acum := lc_volume * lc_ftch_cum; 
            END IF; 

            IF lc_ftch_kg_trif_amnt_acum > lc_ftch_cum_trif_amnt_acum THEN
               RETURN (lc_ftch_kg_trif_amnt_acum);
            ELSE
               RETURN (lc_ftch_cum_trif_amnt_acum);
            END IF;
            
        END IF;
    END fun_ftch_trif_amnt_C;
    
FUNCTION fun_ftch_trif_amnt_C_KM(clnt_id VARCHAR2, orgn_brnc VARCHAR2, km_dist NUMBER,trif_slab VARCHAR2,
    refr_serv_id VARCHAR2, serv_id VARCHAR2,weight NUMBER,VOLUME NUMBER)RETURN NUMBER IS
    lc_weight VARCHAR2(80);
    LC_VOLUME VARCHAR2(80);
    lc_fact_type VARCHAR2(80);
    lc_ftch_non WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    lc_ftch_kg WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    lc_ftch_cum WEB_CLNT_SRVC_TRIF_KM.WT_FACTOR_VALUE%TYPE;
    
    lc_ftch_kg_factor_value WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_FACTOR_VALUE%TYPE;
    lc_ftch_kg_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;
    lc_ftch_kg_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;
    aditional_weight NUMBER;
    
    lc_ftch_cum_factor_value WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_FACTOR_VALUE%TYPE;
    lc_ftch_cum_trif_amnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;
    lc_ftch_cum_trif_amnt_acum WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT%TYPE;
    aditional_volume NUMBER;

    adiAmnt WEB_CLNT_SRVC_TRIF_FACTOR_KM.WCK_TRIF_AMNT_EXED%TYPE := 0;
    flg NUMBER := 1;--FLAG PARA VALIDAR SI EXISTE REGISTRO EN TABLA FACTOR
    
    CURSOR CUR_FTCH_NON_WT_VOL(lc_fact_type VARCHAR2) IS
           SELECT WT_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF_KM
            WHERE WT_ORGN_CLNT_ID = clnt_id AND
                  km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND
                  WT_TRIF_SLAB    = trif_slab    AND
                  WT_REFR_SRVC_ID = refr_serv_id AND
                  WT_SRVC_ID      = serv_id      AND
                  WT_FACTOR          = lc_fact_type;
                  
        CURSOR CUR_FTCH_FACTOR_WT_VOL(lc_fact_type VARCHAR2) IS
           SELECT WCK_TRIF_AMNT, WCK_FACTOR_VALUE, NVL(WCK_TRIF_AMNT_EXED,0)
             FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM
            WHERE WCK_ORGN_CLNT_ID = clnt_id         AND
                  km_dist between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM AND
                  WCK_TRIF_SLAB       = trif_slab    AND
                  WCK_REFR_SRVC_ID = refr_serv_id AND
                  WCK_SRVC_ID          = serv_id      AND
                  WCK_FACTOR           = lc_fact_type
            ORDER BY WCK_FACTOR_VALUE;

        CURSOR cur_ftch_trif_amnt IS
            SELECT WT_TRIF_AMNT
                FROM WEB_CLNT_SRVC_TRIF_KM
                WHERE WT_ORGN_CLNT_ID=clnt_id AND
                    km_dist between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND
                    WT_TRIF_SLAB  = trif_slab AND
                    WT_REFR_SRVC_ID = refr_serv_id AND
                    WT_SRVC_ID = serv_id AND WT_TRIF_AMNT <> 0;
                    
        CURSOR CUR_FTCH_FACTOR_VALID IS
           SELECT WCK_TRIF_AMNT
             FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM
            WHERE WCK_ORGN_CLNT_ID = clnt_id AND
                  km_dist between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM AND
                  WCK_TRIF_SLAB = trif_slab AND
                  WCK_REFR_SRVC_ID = refr_serv_id AND
                  WCK_SRVC_ID = serv_id AND
                  ROWNUM <= 1
            ORDER BY WCK_FACTOR_VALUE;                                
    BEGIN
        lc_ftch_kg_trif_amnt_acum :=0;
        lc_ftch_cum_trif_amnt_acum :=0;
        lc_weight:=weight;
        lc_volume:=volume;
        
        OPEN CUR_FTCH_FACTOR_VALID;
        FETCH CUR_FTCH_FACTOR_VALID INTO lc_ftch_non;
        IF CUR_FTCH_FACTOR_VALID%NOTFOUND THEN
            flg:=0;
        END IF;
        CLOSE CUR_FTCH_FACTOR_VALID;
        
        IF flg=0 THEN
            
            OPEN cur_ftch_trif_amnt;
            FETCH cur_ftch_trif_amnt INTO lc_ftch_non;
            IF cur_ftch_trif_amnt%NOTFOUND THEN
                lc_ftch_non:=0;
            END IF;
            CLOSE cur_ftch_trif_amnt;
            RETURN (lc_ftch_non);
            
        ELSE
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('KG');
            LOOP
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_kg_trif_amnt, lc_ftch_kg_factor_value, adiAmnt;
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN
                    EXIT;
                END IF;
                lc_ftch_kg := adiAmnt;--almacena costo kilo adicional
                lc_ftch_kg_trif_amnt_acum:=lc_ftch_kg_trif_amnt_acum + lc_ftch_kg_trif_amnt;
                
                if lc_weight <= lc_ftch_kg_factor_value then
                    begin
                        aditional_weight := 0;
                        exit;
                    end;
                 else
                    begin
                        aditional_weight := lc_weight - lc_ftch_kg_factor_value;
                    end;
                 end if;                
             END LOOP;
            CLOSE CUR_FTCH_FACTOR_WT_VOL; 
            
            --open CUR_FTCH_NON_WT_VOL('KG');
            /*open CUR_FTCH_NON_WT_VOL('NON');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_kg;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_kg:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;*/
            IF lc_ftch_kg IS NULL THEN 
                lc_ftch_kg :=0; 
            END IF;
            
            IF lc_ftch_kg_trif_amnt_acum IS NULL THEN 
                lc_ftch_kg_trif_amnt_acum :=0; 
            END IF;
            
            IF lc_ftch_kg_trif_amnt_acum > 0 THEN
                IF aditional_weight >0 THEN 
                    lc_ftch_kg_trif_amnt_acum := lc_ftch_kg_trif_amnt_acum + (aditional_weight*lc_ftch_kg); 
                END IF;
            ELSE  
                lc_ftch_kg_trif_amnt_acum := lc_weight * lc_ftch_kg; 
            END IF;
            
            adiAmnt :=0;--se reinicia valor de variable que mantiene costo adicional
            
            OPEN CUR_FTCH_FACTOR_WT_VOL('CUM');
            LOOP
                FETCH CUR_FTCH_FACTOR_WT_VOL INTO lc_ftch_cum_trif_amnt, lc_ftch_cum_factor_value, adiAmnt;
                IF CUR_FTCH_FACTOR_WT_VOL%NOTFOUND THEN
                    EXIT;
                END IF;
                lc_ftch_cum := adiAmnt;--almacena costo volumen adicional
                lc_ftch_cum_trif_amnt_acum:=lc_ftch_cum_trif_amnt_acum + lc_ftch_cum_trif_amnt;
                
                if lc_volume <= lc_ftch_cum_factor_value then
                    begin
                        aditional_volume := 0;
                        exit;
                    end;
                 else
                    begin
                        aditional_volume := lc_volume - lc_ftch_cum_factor_value;
                    end;
                 end if;                
             END LOOP;
            CLOSE CUR_FTCH_FACTOR_WT_VOL;

            /*open CUR_FTCH_NON_WT_VOL('CUM');
            FETCH CUR_FTCH_NON_WT_VOL INTO lc_ftch_cum;

            IF CUR_FTCH_NON_WT_VOL%NOTFOUND THEN
               lc_ftch_cum:=0;
            END IF;
            CLOSE CUR_FTCH_NON_WT_VOL;*/
            IF lc_ftch_cum IS NULL THEN 
                lc_ftch_cum :=0; 
            END IF;

            IF lc_ftch_cum_trif_amnt_acum IS NULL THEN 
                lc_ftch_cum_trif_amnt_acum :=0; 
            END IF;
            
            IF lc_ftch_cum_trif_amnt_acum > 0 THEN 
                IF aditional_volume >0 THEN 
                    lc_ftch_cum_trif_amnt_acum := lc_ftch_cum_trif_amnt_acum + (aditional_volume*lc_ftch_cum); 
                END IF;
            ELSE  
                lc_ftch_cum_trif_amnt_acum := lc_volume * lc_ftch_cum; 
            END IF;

            IF lc_ftch_kg_trif_amnt_acum > lc_ftch_cum_trif_amnt_acum THEN
               RETURN (lc_ftch_kg_trif_amnt_acum);
            ELSE
               RETURN (lc_ftch_cum_trif_amnt_acum);
            END IF;

        END IF;
    END fun_ftch_trif_amnt_C_KM;
FUNCTION fun_ftch_trif_amnt_new(clnt_id VARCHAR2,dest_brnc VARCHAR2, serv_id VARCHAR2, tarifa VARCHAR2)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT TD_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF_DTL
        WHERE TD_ORGN_CLNT_ID = clnt_id
        AND substr(TD_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)
        AND TD_SRVC_ID = serv_id
        AND TD_TRIF_ID = tarifa
        AND TD_TRIF_AMNT <> 0;
    ln_amnt NUMBER;
BEGIN
    OPEN cur_ftch_trif_amnt;
    FETCH cur_ftch_trif_amnt INTO ln_amnt;
    IF cur_ftch_trif_amnt%NOTFOUND THEN
        ln_amnt:=0;
    END IF;
    CLOSE cur_ftch_trif_amnt;
    RETURN(ln_amnt);
END;
FUNCTION fun_ftch_trif_amnt_new_brnc(clnt_id VARCHAR2,dest_brnc VARCHAR2, serv_id VARCHAR2, tarifa VARCHAR2, orgn_brnc VARCHAR2)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT TD_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF_DTL
        WHERE TD_ORGN_CLNT_ID = clnt_id
        AND substr(TD_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)
        AND substr(TD_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)
        AND TD_SRVC_ID = serv_id
        AND TD_TRIF_ID = tarifa
        AND TD_TRIF_AMNT <> 0;
    ln_amnt NUMBER;
BEGIN
    OPEN cur_ftch_trif_amnt;
    FETCH cur_ftch_trif_amnt INTO ln_amnt;
    IF cur_ftch_trif_amnt%NOTFOUND THEN
        ln_amnt:=0;
    END IF;
    CLOSE cur_ftch_trif_amnt;
    RETURN(ln_amnt);
END;
FUNCTION fun_ftch_trif_amnt_new_km(clnt_id VARCHAR2, km_dist NUMBER, serv_id VARCHAR2, tarifa VARCHAR2)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT TD_TRIF_AMNT
        FROM WEB_CLNT_SRVC_TRIF_DTL_KM
        WHERE TD_ORGN_CLNT_ID = clnt_id
        AND km_dist between TD_DSTN_FROM_KM and TD_DSTN_TO_KM
        AND TD_SRVC_ID = serv_id
        AND TD_TRIF_ID = tarifa
        AND TD_TRIF_AMNT <> 0;
    ln_amnt NUMBER;
BEGIN
    OPEN cur_ftch_trif_amnt;
    FETCH cur_ftch_trif_amnt INTO ln_amnt;
    IF cur_ftch_trif_amnt%NOTFOUND THEN
        ln_amnt:=0;
    END IF;
    CLOSE cur_ftch_trif_amnt;
    RETURN(ln_amnt);
END;
------------------
--PROCEDURE
   --(addr_code varchar2,u11 out varchar2,u12 out varchar2,u13 out varchar2,
   --u14 out varchar2,u15 out varchar2,u16 out varchar2,u17 out varchar2,zipcode out varchar2,
   --c11 out varchar2,c12 out varchar2,c13 out varchar2,c14 out varchar2,c15 out varchar2,c16 out varchar2) IS
PROCEDURE pro_ftch_addr(am_defn_type VARCHAR2, am_ref_no VARCHAR2, am_gcode VARCHAR2,
    am_glevl NUMBER, am_gtype NUMBER,
     u11 OUT VARCHAR2,u12 OUT VARCHAR2,u13 OUT VARCHAR2,
   u14 OUT VARCHAR2,u15 OUT VARCHAR2,u16 OUT VARCHAR2,u17 OUT VARCHAR2,zipcode OUT VARCHAR2,
   c11 OUT VARCHAR2,c12 OUT VARCHAR2,c13 OUT VARCHAR2,c14 OUT VARCHAR2,c15 OUT VARCHAR2,c16 OUT VARCHAR2) IS
    --am_defn_type varchar2(15);
    --am_ref_no varchar2(15);
    --am_gcode varchar2(15);
    u1 VARCHAR2(60);u2 VARCHAR2(60);
    --am_glevl varchar2(15);
    --am_gtype varchar2(15);
    u3 VARCHAR2(60);u4 VARCHAR2(60);u5 VARCHAR2(60);
    u6 VARCHAR2(60);u7 VARCHAR2(60);c1 VARCHAR2(50);
    c2 VARCHAR2(50);c3 VARCHAR2(50);c4 VARCHAR2(50);
    c5 VARCHAR2(50);c6 VARCHAR2(50);dir_alt VARCHAR2(1);

    --cursor cur_ftch_addr(lv_addr_code varchar2) is
    --select AM_ADDR_DEFN_TYPE, AM_GETY_CODE, AM_ADDR_REF_NO,
    --AM_GETY_LEVL, AM_GETY_TYPE
    --from sys_addr_mstr where am_addr_code=lv_addr_code;
BEGIN
    --open cur_ftch_addr(addr_code);
    --fetch cur_ftch_addr into am_defn_type,am_gcode,am_ref_no, am_glevl, am_gtype;
    --if cur_ftch_addr%notfound then
  ---        am_defn_type := '';
    --    am_gcode := '';
--        am_ref_no := '';
    --    am_glevl := '';
        --am_gtype := '';
    --end if;
    --close cur_ftch_addr;

          IF am_defn_type='N' THEN
              --Pack_Web.pro_ftch_unst(am_ref_no,u1,u2,u3,u4,u5,u6,u7);
              Pack_Web.pro_ftch_unst_dir_alt(am_ref_no,u1,u2,u3,u4,u5,u6,u7,dir_alt);
              u11 := u1;  u12 := u2;  u13 := u3;  u14 := u4;
              u15 := u5;  u16 := u6;  u17 := u7;zipcode := u7;
              c11 := NULL;c12:= NULL; c13 :=NULL;c14 := NULL;
              c15 := NULL;c16:= NULL;
              IF (dir_alt IS NOT NULL AND dir_alt='P') THEN
                Pack_Web.pro_get_geoenty(am_gcode,u1,u2,u3,u4,u5,u6,u7,c1,c2,c3,c4,c5,c6,zipcode, am_glevl, am_gtype); -- parameters level and type added on 13-03-2003 by Hari
              --zipcode := fun_get_zipcode(C6,am_glevl,am_gtype);
                u11 := u1;  u12 := u2;  u13 := u3;  u14 := u4;
                u15 := u5;  u16 := u6;  u17 := u7;
                c11 := c1;  c12 := c2;  c13 := c3;  c14 := c4;
                c15 := c5;  c16 := c6;
              END IF;
          ELSE
              Pack_Web.pro_get_geoenty(am_gcode,u1,u2,u3,u4,u5,u6,u7,c1,c2,c3,c4,c5,c6,zipcode, am_glevl, am_gtype); -- parameters level and type added on 13-03-2003 by Hari
              --zipcode := fun_get_zipcode(C6,am_glevl,am_gtype); -- parameters level and type added on 13-03-2003 by Hari
              u11 := u1;  u12 := u2;  u13 := u3;  u14 := u4;
              u15 := u5;  u16 := u6;  u17 := u7;
              c11 := c1;  c12 := c2;  c13 := c3;  c14 := c4;
              c15 := c5;  c16 := c6;
          END IF;
END;
----------------------
PROCEDURE pro_ftch_unst(ref_no VARCHAR2,P1 OUT VARCHAR2,p2 OUT VARCHAR2,
    p3 OUT VARCHAR2,p4 OUT VARCHAR2,p5 OUT VARCHAR2,p6 OUT VARCHAR2,p7 OUT VARCHAR2) IS
    CURSOR cur_ftch_unst(lv_ref_no VARCHAR2) IS
    SELECT CA_LEVL_1, CA_LEVL_2, CA_LEVL_3, CA_LEVL_4, CA_LEVL_5, CA_LEVL_6, CA_LEVL_7
    FROM SYS_CUST_ADDR WHERE CA_ADDR_ID=lv_ref_no;
BEGIN
    OPEN cur_ftch_unst(ref_no);
    FETCH cur_ftch_unst INTO P1,p2,p3,p4,p5,p6,p7;
    IF cur_ftch_unst%NOTFOUND THEN
        P1 := '';
        p2 := '';
        p3 := '';
        p4 := '';
        p5 := '';
        p6 := '';
        p7 := '';
    END IF;
    CLOSE cur_ftch_unst;
END;
PROCEDURE pro_ftch_unst_dir_alt(ref_no VARCHAR2,P1 OUT VARCHAR2,p2 OUT VARCHAR2,
    p3 OUT VARCHAR2,p4 OUT VARCHAR2,p5 OUT VARCHAR2,p6 OUT VARCHAR2,p7 OUT VARCHAR2,dir_alt OUT VARCHAR2) IS
    CURSOR cur_ftch_unst(lv_ref_no VARCHAR2) IS
    SELECT CA_LEVL_1, CA_LEVL_2, CA_LEVL_3, CA_LEVL_4, CA_LEVL_5, CA_LEVL_6, CA_LEVL_7, CA_DIR_ALT
    FROM SYS_CUST_ADDR WHERE CA_ADDR_ID=lv_ref_no;
BEGIN
    OPEN cur_ftch_unst(ref_no);
    FETCH cur_ftch_unst INTO P1,p2,p3,p4,p5,p6,p7,dir_alt;
    IF cur_ftch_unst%NOTFOUND THEN
        P1 := '';
        p2 := '';
        p3 := '';
        p4 := '';
        p5 := '';
        p6 := '';
        p7 := '';
        dir_alt := '';
    END IF;
    CLOSE cur_ftch_unst;
END;
----------------------
PROCEDURE pro_get_geoenty(lc_mpc IN VARCHAR2, levl1 OUT VARCHAR2,levl2 OUT VARCHAR2,
levl3 OUT VARCHAR2,/*AAP06*/levl5 OUT VARCHAR2,/*AAP06*/levl4 OUT VARCHAR2,levl6 OUT VARCHAR2,levl7 OUT VARCHAR2,
levc1 OUT VARCHAR2, levc2 OUT VARCHAR2, levc3 OUT VARCHAR2,/*AAP06*/levc5 OUT VARCHAR2,/*AAP06*/levc4 OUT VARCHAR2,
levc6 OUT VARCHAR2,zipcode OUT VARCHAR2, lc_lvl IN VARCHAR2, lc_typ IN VARCHAR2) IS
   ln_lr  NUMBER;
   ln_tr  NUMBER;
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(500);
   lc_gdesc1 VARCHAR2(5000);
   lc_zipcode VARCHAR2(100);
   MSGNUM NUMBER;
   CURSOR pro_gety_c1(lv_lc_mpc VARCHAR2, lv_lc_lvl VARCHAR2, lv_lc_typ VARCHAR2) IS
           SELECT GE_DESC,GE_CODE,ZIP_CODE FROM SYS_GETY_MSTR WHERE GE_CODE=LV_LC_MPC
        AND GE_LEVL = lv_lc_lvl AND GE_TYPE = lv_lc_typ; -- added by Hari on 13-03-2003
   CURSOR pro_gety_c2(lv_lc_mpc VARCHAR2, lv_lc_lvl VARCHAR2, lv_lc_typ VARCHAR2) IS
      SELECT DECODE(GR_LEVL_R,6,6,5,66,NULL),GR_TYPE_R,GR_CODE_R,GE_DESC,ZIP_CODE
         FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
         WHERE GR_CODE = lv_lc_mpc
        AND GR_LEVL = lv_lc_lvl AND GR_TYPE = lv_lc_typ -- added by Hari on 13-03-2003
        AND GM.GE_LEVL=GR.GR_LEVL_R    AND GM.GE_TYPE=GR.GR_TYPE_R
        AND GM.GE_CODE=GR.GR_CODE_R;
   CURSOR pro_gety_c3(lv_ln_lr NUMBER,lv_ln_tr NUMBER,lv_lc_mcd VARCHAR2) IS
           SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC,ZIP_CODE
         FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
         WHERE GR_LEVL=lv_ln_lr AND GR_TYPE=lv_ln_tr
      AND GR_CODE=lv_lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R AND GM.GE_CODE=GR.GR_CODE_R;
BEGIN
    BEGIN
    --dbms_output.put_line('he');
    -- parameters geo entity level and type added by Hari on 13-03-2003
        OPEN pro_gety_c1(lc_mpc, lc_lvl, lc_typ);
      FETCH pro_gety_c1 INTO LEVL6,LEVC6,zipcode;
      IF pro_gety_c1%NOTFOUND THEN
           NULL;
      END IF;
      CLOSE pro_gety_c1;
      EXCEPTION WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('FIRST-'||SQLERRM);

    END;
     --DECODE(GR_LEVL_R,6,6,5,66,null) --:ctrl.ZIP_CODE
     -- parameters geo entity level and type added by Hari on 13-03-2003
    BEGIN
        OPEN pro_gety_c2(lc_mpc, lc_lvl, lc_typ);
      FETCH pro_gety_c2 INTO ln_lr,ln_tr,lc_mcd,lc_gdesc,lc_zipcode;
      IF pro_gety_c2%NOTFOUND THEN
          lc_gdesc1 := NULL;
      END IF;
      CLOSE pro_gety_c2;

      EXCEPTION WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('SECOND-'||SQLERRM);
      END;

    IF ln_lr=66 THEN
         levl5 := UPPER(lc_gdesc);
         ln_lr := 5;
         levc5 := lc_mcd;
    END IF;
   --LOOP
   BEGIN
           OPEN pro_gety_c3(ln_lr,ln_tr,lc_mcd);
           FETCH pro_gety_c3 INTO ln_lr,ln_tr,lc_mcd,lc_gdesc,lc_zipcode;
           IF pro_gety_c3%NOTFOUND THEN
               NULL;
           END IF;
           CLOSE pro_gety_c3;

      EXCEPTION WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('THIRD-'||SQLERRM);
      END;

           lc_gdesc1 :=lc_gdesc1||', '||RTRIM(lc_gdesc) ;
           IF ln_lr = 1 THEN
                levl1 := UPPER(lc_gdesc);
                levc1 := lc_mcd;
           END IF;
           IF ln_lr = 2 THEN
                levl2 := UPPER(lc_gdesc);
                levc2 := lc_mcd;
           END IF;
           IF ln_lr = 3 THEN
                levl3 := UPPER(lc_gdesc);
                levc3 := lc_mcd;
           END IF;
           IF ln_lr = 4 THEN
                levl4 := UPPER(lc_gdesc);
                levc4 := lc_mcd;
           END IF;
           IF ln_lr = 5 THEN
                levl5 := UPPER(lc_gdesc);
                levc5 := lc_mcd;
             END IF;
        --   IF LN_LR=1 AND LN_TR=1 THEN
          --     EXIT;
           --END IF;
    -- END LOOP;
END PRO_GET_GEOENTY;
/*------------------
    PROCEDURE pro_ftch_dflt_coln_addr(asgn_brnc VARCHAR2, clnt_id VARCHAR2, strt_name VARCHAR2,
        drnr VARCHAR2, coly VARCHAR2, city VARCHAR2,
        lc_clnt VARCHAR2(4):='CLNT';
        lc_y    VARCHAR2(4):='Y';
        CURSOR cur_ftch_dflt_coln_addr IS
            SELECT AM_ADDR_REF_NO, AM_STRT_NAME, AM_PHNO1, AM_DRNR, AM_GETY_LEVL, AM_GETY_TYPE,
                AM_GETY_CODE
                FROM sys_addr_mstr
                WHERE AM_PE_BRNC_ID=asgn_brnc AND AM_ENTY_ID=clnt_id
                AND AM_ADDR_TYPE=lc_clnt AND AM_DEFA_FLAG=lc_y;
    BEGIN
        OPEN cur_ftch_dflt_coln_addr;
        FETCH cur_ftch_dflt_coln_addr INTO

    END;*/
------------------
/* CREATED BY VISHNU ON 18TH OF MARCH 2003
    FUNCTIONALITY - TO FETCH THE MANIFEST DESCRIPTION WHEN GIVEN THE REQUIRED PARAMETERS */
FUNCTION FUN_MNFT_STUS_DESC(MDUL_ID VARCHAR2,PARM_TYPE VARCHAR2,PARM_CODE1 VARCHAR2)
    RETURN VARCHAR2 IS
    LC_MNFT_STUS_DESC SYS_PARM_MSTR.PM_VLUE1_DESC%TYPE;
    CURSOR CUR_MNFT_STUS_DESC IS
        SELECT PM_VLUE1_DESC
            FROM SYS_PARM_MSTR
            WHERE PM_MDUL_ID = MDUL_ID AND
                PM_PARM_TYPE = PARM_TYPE AND
                PM_PARM_CODE1 = PARM_CODE1;
    BEGIN
        OPEN CUR_MNFT_STUS_DESC;
    FETCH CUR_MNFT_STUS_DESC INTO LC_MNFT_STUS_DESC;
        CLOSE  CUR_MNFT_STUS_DESC;
        RETURN LC_MNFT_STUS_DESC;
    END;
------------------
  FUNCTION fun_ftch_dely_type(guia_no VARCHAR2, guia_type VARCHAR2, actv_flag VARCHAR2)
      RETURN VARCHAR2 IS
        LV_GUIA_EAD CHAR(1);
        lc_dely_type SYS_PARM_MSTR.PM_VLUE1_DESC%TYPE;
        CURSOR CUR_FTCH_SRVC_EAD IS
            SELECT GH_EAD_FLAG
            FROM BOK_GUIA_HEAD
            WHERE GH_GUIA_NO = guia_no AND
            GH_GUIA_TYPE = guia_type AND
            GH_ACTV_FLAG = actv_flag;
        CURSOR CUR_GUIA_STUS(LV_MDUL VARCHAR2,LV_PTYPE VARCHAR2,LV_PCODE VARCHAR2) IS
            SELECT PM_VLUE1_DESC FROM SYS_PARM_MSTR
            WHERE PM_MDUL_ID = LV_MDUL AND
            PM_PARM_TYPE = LV_PTYPE AND
            PM_PARM_CODE1 = LV_PCODE;
    BEGIN
         OPEN CUR_FTCH_SRVC_EAD;
        FETCH CUR_FTCH_SRVC_EAD INTO LV_GUIA_EAD;
        IF LV_GUIA_EAD = 0 THEN
            --'BRANCH DELIVERY';
            OPEN CUR_GUIA_STUS('BOK','DELIVERY','O');
            FETCH CUR_GUIA_STUS INTO lc_dely_type;
            IF CUR_GUIA_STUS%NOTFOUND THEN
                lc_dely_type := NULL;
            END IF;
            CLOSE CUR_GUIA_STUS;
        ELSE
            --'OFFICE DELIVERY';
            OPEN CUR_GUIA_STUS('BOK','DELIVERY','H');
            FETCH CUR_GUIA_STUS INTO lc_dely_type;
            IF CUR_GUIA_STUS%NOTFOUND THEN
                lc_dely_type := NULL;
            END IF;
            CLOSE CUR_GUIA_STUS;
        END IF;
        IF CUR_FTCH_SRVC_EAD%NOTFOUND THEN
                lc_dely_type:='OCURRE';
        END IF;
        CLOSE CUR_FTCH_SRVC_EAD;
        RETURN(lc_dely_type);
    END;
------------------
    PROCEDURE PRO_SHOW_MESG(module_id VARCHAR2,lang_id VARCHAR2,mesg_id NUMBER,
        mesg_subid NUMBER,var1 VARCHAR2,var2 VARCHAR2,var3 VARCHAR2,mesg_type OUT VARCHAR2,
        mesg_text OUT VARCHAR2) IS
        lv_mesg SYS_MESG_MSTR.MM_MESG_TEXT%TYPE;
        lv_mesg_type SYS_MESG_MSTR.MM_MESG_TYPE%TYPE;
        ln_defa_but SYS_MESG_MSTR.MM_DEFA_BUTN%TYPE;
        lb_activ SYS_MESG_MSTR.MM_ACTV%TYPE;
        ld_till SYS_MESG_MSTR.MM_ACTV_TILL%TYPE;
        --ln_alert number;
        --lv_alert_title varchar2(50);
    BEGIN
        /*call to procedure Get Message*/
        Pack_Web.PRO_GET_MESG(module_id,lang_id,mesg_id,mesg_subid,lv_mesg_type,lv_mesg,ln_defa_but,lb_activ,ld_till,var1,var2,var3);
        --Message type below will be either null or any valid one.
        mesg_type:=lv_mesg_type;
        mesg_text:=lv_mesg;
        /*--designing alert
        --setting the alert window title
        if lv_mesg_type = 'INFO' then
            lv_alert_title := 'INFORMATION';
        elsif lv_mesg_type = 'ERRO' then
            lv_alert_title := 'ERROR';
        elsif lv_mesg_type = 'PRMO' then
            lv_alert_title := 'PROMOTIONAL';
        elsif lv_mesg_type = 'QUST' then
            lv_alert_title := 'QUESTION';
        elsif lv_mesg_type = 'WARN' then
            lv_alert_title := 'WARNING';
        end if;
        --IF lv_mesg_type = 'HINT' OR lv_mesg_type = 'TITL' THEN
        IF lv_mesg_type = 'HINT' THEN
            MESSAGE(lv_mesg);
            RETURN 0;
        END IF;
        --if lv_mesg_type = 'INFO' or lv_mesg_type = 'ERRO' or lv_mesg_type =  'PRMO' then
        if lv_mesg_type = 'INFO' or lv_mesg_type = 'ERRO' then
            set_alert_property('ALERT1',alert_message_text,lv_mesg);
            set_alert_property('ALERT1',title,lv_alert_title);
            ln_alert := show_alert('ALERT1');
        end if;
        if lv_mesg_type = 'QUST' then
            set_alert_property('ALERT2',alert_message_text,lv_mesg);
            set_alert_property('ALERT2',title,lv_alert_title);
            ln_alert := show_alert('ALERT2');
        end if;
        if lv_mesg_type = 'WARN' then
            set_alert_property('ALERT3',alert_message_text,lv_mesg);
            set_alert_property('ALERT3',title,lv_alert_title);
            ln_alert := show_alert('ALERT3');
        end if;
        return(ln_alert); */
    END;

    PROCEDURE PRO_GET_MESG(module_id VARCHAR,lang_id VARCHAR,
    mesg_id NUMBER,mesg_subid NUMBER,lv_mesg_type IN OUT VARCHAR,lv_mesg IN OUT VARCHAR,
    ln_defa_but IN OUT NUMBER,lb_activ IN OUT VARCHAR,ld_till IN OUT DATE,var1 IN VARCHAR,
    var2 IN VARCHAR,var3 IN VARCHAR)    IS
    lv_mesg1             SYS_MESG_MSTR.MM_MESG_TEXT%TYPE;
    lv_mesg2             SYS_MESG_MSTR.MM_MESG_TEXT%TYPE;
    lv_mesg_type1     SYS_MESG_MSTR.MM_MESG_TYPE%TYPE;
    ln_defa_but1     SYS_MESG_MSTR.MM_DEFA_BUTN%TYPE;
    lb_activ1             SYS_MESG_MSTR.MM_ACTV%TYPE;
    ld_till1             SYS_MESG_MSTR.MM_ACTV_TILL%TYPE;
    lv_activ_promo SYS_MESG_MSTR.MM_MESG_TEXT%TYPE;
        pos NUMBER;
        cnt NUMBER := 0;
        v1 VARCHAR2(100) := NVL(var1,' ');
        v2 VARCHAR2(100) := NVL(var2,' ');
        v3 VARCHAR2(100) := NVL(var3,' ');
        amp VARCHAR2(80);
        amp1 CHAR(2);
        FLAG NUMBER(1) := 0;
        CURSOR cur_Get_mesg(lc_mdul_id IN VARCHAR2, lc_lang_id IN VARCHAR2,
                lc_mesg_id IN VARCHAR2,lc_mesg_subid IN VARCHAR2) IS
        SELECT MM_MESG_TYPE,MM_MDUL_ID||'-'||MM_MESG_ID||' '||MM_MESG_TEXT,MM_DEFA_BUTN,MM_ACTV,MM_ACTV_TILL
        FROM SYS_MESG_MSTR
        WHERE MM_MDUL_ID = lc_mdul_id
        AND MM_LANG_ID = lc_lang_id
        AND MM_MESG_ID = lc_mesg_id
        AND NVL(MM_MESG_SUB_ID,1) = NVL(lc_mesg_subid,1);
    BEGIN
        OPEN cur_Get_mesg(module_id,lang_id,mesg_id,mesg_subid);
        FETCH cur_Get_mesg INTO lv_mesg_type1,lv_mesg1,ln_defa_but1,lb_activ1,ld_till1;
        IF cur_get_mesg%NOTFOUND THEN
            lv_mesg_type:=NULL;
            lv_mesg:='Sorry! No record exists for this message code - '||mesg_id;
        ELSE
            lv_mesg := lv_mesg1;
            lv_mesg_type := lv_mesg_type1;
            ln_defa_but := ln_defa_but1;
            lb_activ := lb_activ1;
            ld_till := ld_till1;
            --positional substitution of '#' for special messages
            FOR i IN 1..3
                LOOP
                pos := INSTR(lv_mesg1,'#',1,i);
                IF pos > 0 THEN
                    cnt := cnt+1;
                    amp := SUBSTR(lv_mesg1,pos,pos+2);
                    amp1 := SUBSTR(amp,1,2);
                    IF cnt = 1 THEN
                        lv_mesg2 := REPLACE(lv_mesg1,amp1,v1);
                        FLAG := 1;
                    END IF;
                    IF cnt = 2 THEN
                        lv_mesg2 := REPLACE(lv_mesg2,amp1,v2);
                        FLAG := 1;
                    END IF;
                    IF cnt = 3 THEN
                        lv_mesg2 := REPLACE(lv_mesg2,amp1,v3);
                        FLAG := 1;
                    END IF;
                lv_mesg := lv_mesg2;
                END IF;
                IF FLAG <> 1 THEN
                    lv_mesg := lv_mesg1;
                END IF;
            END LOOP;
            --if lv_mesg_type = 'PRMO' then
            IF lv_mesg_type IN ('PRMO', 'TITL') THEN
                lv_mesg_type:=NULL;
                lv_mesg:='Sorry! No record exists for this message code - '||mesg_id;
                /*if lb_activ1 != 'T' then
                    message('Sorry unable to display message - It is currently inactive');
                    PAUSE;
                    RAISE FORM_TRIGGER_FAILURE;
                end if;*/
            END IF;
            IF ld_till > SYSDATE THEN
                lv_mesg_type:=NULL;
                lv_mesg:='Sorry unable to display message - Message Tenure Expired';
            END IF;
        END IF;
        CLOSE cur_Get_mesg;
    END;
------------------
    FUNCTION Fun_ftch_brnc_name(Brnc_id IN VARCHAR2) RETURN VARCHAR2 IS
        lv_brnc_name SYS_BRNC_MSTR.BM_BRNC_NAME%TYPE;
        CURSOR CUR_GET_BRN_NAME(LV_BRNC_ID VARCHAR2) IS
            SELECT BM_BRNC_NAME
             FROM SYS_BRNC_MSTR
             WHERE BM_BRNC_ID=LV_BRNC_ID;
    BEGIN
        OPEN CUR_GET_BRN_NAME(BRNC_ID);
        FETCH CUR_GET_BRN_NAME INTO LV_BRNC_NAME;
        IF CUR_GET_BRN_NAME%NOTFOUND THEN
            LV_BRNC_NAME := NULL;
        END IF;
        CLOSE CUR_GET_BRN_NAME;
        RETURN(lv_brnc_name);
    END;
------------------
/* At present it is not used */
  FUNCTION FUN_GET_GUIA_STUS(guia_no NUMBER) RETURN VARCHAR2 IS
        guia_stat VARCHAR2(15) := NULL;
        CURSOR cur_get_guia_stus(lc_guia_no IN VARCHAR2) IS
      SELECT GH_GUIA_STUS
      FROM BOK_GUIA_HEAD
      WHERE GH_GUIA_NO = lc_guia_no ;
    BEGIN
        OPEN cur_get_guia_stus(TO_NUMBER(guia_no));
        FETCH cur_get_guia_stus INTO guia_stat;
        CLOSE cur_get_guia_stus;
      RETURN guia_stat;
    END;
------------------
    FUNCTION Fun_ftch_guia_stus_desc(guia_stus IN VARCHAR2) RETURN VARCHAR2 IS
        stus_desc SYS_PARM_MSTR.PM_VLUE1_DESC%TYPE;
        CURSOR CUR_GUIA_STUS(LV_MDUL VARCHAR2,LV_PTYPE VARCHAR2,LV_PCODE VARCHAR2) IS
            SELECT PM_VLUE1_DESC FROM SYS_PARM_MSTR
            WHERE PM_MDUL_ID = LV_MDUL AND
            PM_PARM_TYPE = LV_PTYPE AND
            PM_PARM_CODE1 = LV_PCODE;
    BEGIN
        OPEN CUR_GUIA_STUS('BOK','GUIA_STATUS',guia_stus);
        FETCH CUR_GUIA_STUS INTO stus_desc;
        IF CUR_GUIA_STUS%NOTFOUND THEN
            stus_desc := NULL;
        END IF;
        CLOSE CUR_GUIA_STUS;
        RETURN stus_desc;
    END;
------------------
    FUNCTION Fun_ftch_cod_vlue(guia_no IN VARCHAR2) RETURN NUMBER IS
        LN_COD_VALUE  BOK_GUIA_SRVC_ITEM.GL_VLUE_1%TYPE;
        CURSOR CUR_GET_COD(LV_SRVC VARCHAR2,LV_REF_SRVC VARCHAR2,LV_GUIA VARCHAR2) IS
            SELECT GL_VLUE_1
            FROM BOK_GUIA_SRVC_ITEM
            WHERE GL_GUIA_NO = LV_GUIA
            AND GL_SRVC_ID = LV_SRVC
            AND GL_REFR_SRVC_ID = LV_REF_SRVC;
        BEGIN
            OPEN CUR_GET_COD('CODR-1','CODR',guia_no);
            FETCH CUR_GET_COD INTO LN_COD_VALUE;
            IF CUR_GET_COD%NOTFOUND THEN
                LN_COD_VALUE := NULL;
            END IF;
            CLOSE CUR_GET_COD;
        RETURN LN_COD_VALUE;
    END;
------------------
    /*PROCEDURE pro_ftch_curr_guia_addr(type1 IN VARCHAR2, guia_no In Varchar2,
        GA_STRT_NAME OUT VARCHAR2, GA_DRNR OUT VARCHAR2,GA_ADDR_LIN4 OUT VARCHAR2,
        GA_ADDR_LIN6 OUT VARCHAR2, GA_PHNO_1 OUT VARCHAR2) IS*/
    PROCEDURE pro_ftch_curr_guia_addr(type1 IN VARCHAR2, guia_no IN VARCHAR2,
        lc_strt OUT VARCHAR2, lc_drnr OUT VARCHAR2, lc_lin4 OUT VARCHAR2, lc_lin6 OUT VARCHAR2,
        lc_pho1 OUT VARCHAR2) IS
        /*;
        GA_STRT_NAME OUT VARCHAR2, GA_DRNR OUT VARCHAR2,GA_ADDR_LIN4 OUT VARCHAR2,
        GA_ADDR_LIN6 OUT VARCHAR2, GA_PHNO_1 OUT VARCHAR2) IS*/

        /*lc_strt BOK_GUIA_ADDR.GA_STRT_NAME%TYPE;
        lc_drnr BOK_GUIA_ADDR.GA_DRNR%TYPE;
        lc_lin4 BOK_GUIA_ADDR.GA_ADDR_LIN4%TYPE;
        lc_lin6 BOK_GUIA_ADDR.GA_ADDR_LIN6%TYPE;
        lc_pho1 BOK_GUIA_ADDR.GA_PHNO_1%TYPE;*/

        CURSOR CUR_GET_ADDR(LV_ADDR_TYPE VARCHAR2,LV_GUIA_NO VARCHAR2,LV_ADDR_STAT VARCHAR2) IS
            SELECT GA_STRT_NAME,GA_DRNR,GA_ADDR_LIN4,GA_ADDR_LIN6,GA_PHNO_1
            FROM BOK_GUIA_ADDR
            WHERE GA_ADDR_TYPE = LV_ADDR_TYPE AND GA_GUIA_NO = LV_GUIA_NO
            AND GA_ADDR_STAT = LV_ADDR_STAT;
        BEGIN
            OPEN CUR_GET_ADDR(type1, guia_no, 'C');
            FETCH CUR_GET_ADDR INTO
                lc_strt, lc_drnr, lc_lin4, lc_lin6, lc_pho1;
                IF CUR_GET_ADDR%NOTFOUND THEN
                    lc_strt:=NULL;
                    lc_drnr:=NULL;
                    lc_lin4:=NULL;
                    lc_lin6:=NULL;
                    lc_pho1:=NULL;
                END IF;
            CLOSE CUR_GET_ADDR;
        END;
/*Web Guia booking - start*/
/*Web Guia booking - end*/

/*Web Guia Detail*/
    --Web Guia Detail Shipment Service
    FUNCTION fun_ftch_ship_desc(ship_code VARCHAR2, srvc VARCHAR2, refr_srvc VARCHAR2)
      RETURN VARCHAR2 IS
      lc_ship_desc     SYS_SHP_DESC_MSTR.ss_desc%TYPE;
      CURSOR cur_ftch_ship_desc IS
        SELECT ss_desc
          FROM SYS_SHP_DESC_MSTR
          WHERE SS_CODE=ship_code AND SS_SRVC_ID= srvc AND SS_REFR_SRVC_ID=refr_srvc;
    BEGIN
      OPEN cur_ftch_ship_desc;
      FETCH cur_ftch_ship_desc INTO lc_ship_desc;
      CLOSE cur_ftch_ship_desc;
      RETURN lc_ship_desc;
    END;
------------------
  FUNCTION fun_ftch_dlvy_conc(guia_no VARCHAR2) RETURN VARCHAR2 IS
      lc_dlvy_conc COM_CTT.CT_PRIM_ENTY%TYPE;
        CURSOR CUR_FETCH_PRIM_ENTY(LV_TYPE1 VARCHAR2,LV_TYPE2 VARCHAR2,LV_TYPE3 VARCHAR2) IS
            SELECT CT_PRIM_ENTY
            FROM COM_CTT
            WHERE CT_TRNS_TYPE IN (LV_TYPE1,LV_TYPE2,LV_TYPE3)
            AND CT_SECO_ENTY=GUIA_NO ;
    BEGIN
         OPEN CUR_FETCH_PRIM_ENTY('BDL','PDC','HDC');
        FETCH CUR_FETCH_PRIM_ENTY INTO lc_dlvy_conc;
        IF CUR_FETCH_PRIM_ENTY%NOTFOUND THEN
            lc_dlvy_conc:=NULL;
        END IF;
        CLOSE CUR_FETCH_PRIM_ENTY;
        RETURN(lc_dlvy_conc);
    END;
------------------
    FUNCTION fun_ftch_dlvy_conc_name(dlvy_conc VARCHAR2, dest_clnt_id VARCHAR2,
        dest_clnt_name VARCHAR2) RETURN VARCHAR2 IS
       lc_conc_name SYS_CONC_MSTR.CO_CONC_NAME%TYPE;
        CURSOR CUR_CON1 IS
            SELECT CO_CONC_NAME
            FROM SYS_CONC_MSTR
            WHERE CO_CONC_ID = dlvy_conc AND CO_ENTY_ID = dest_clnt_id;
    BEGIN
        OPEN CUR_CON1;
        FETCH CUR_CON1 INTO lc_conc_name;
        IF CUR_CON1%NOTFOUND THEN
            lc_conc_name := dest_clnt_name;
        END IF;
        CLOSE CUR_CON1;
        RETURN(lc_conc_name);
    END;
------------------
FUNCTION fun_ftch_bok_conc_name(bok_conc_id VARCHAR2) RETURN VARCHAR2 IS
    lc_conc_name SYS_CLNT_MSTR.CM_CLNT_NAME%TYPE;
    CURSOR cur_conc_name1 IS
         SELECT CO_CONC_NAME
         FROM SYS_CONC_MSTR
         WHERE CO_CONC_ID = bok_conc_id;

    CURSOR cur_conc_name2 IS
        SELECT CM_CLNT_NAME
        FROM SYS_CLNT_MSTR
        WHERE CM_CLNT_ID = bok_conc_id;
BEGIN
  OPEN cur_conc_name1;
  FETCH cur_conc_name1 INTO lc_conc_name;
  IF cur_conc_name1%NOTFOUND THEN
    OPEN cur_conc_name2;
    FETCH cur_conc_name2 INTO lc_conc_name;
    CLOSE cur_conc_name2;
  END IF;
  CLOSE cur_conc_name1;
  RETURN(lc_conc_name);
END;
------------------
--Added by Nirmala on 06/06/2003
FUNCTION FUN_FTCH_ADD_SRVC_DISC_guia(guia_no VARCHAR2, srvc_id VARCHAR2) RETURN NUMBER IS
  ln_disc BOK_GUIA_SRVC.gs_disc%TYPE;
    CURSOR CUR_GET_ADDSRVC IS
        SELECT (NVL(gs_sub_totl,0) + NVL(gs_tax,0))-(NVL(gs_disc,0) + NVL(gs_tax_ret,0))
        FROM bok_guia_srvc
         WHERE gs_guia_no  = guia_no
         AND gs_srvc_id = srvc_id;
BEGIN
    OPEN CUR_GET_ADDSRVC;
    FETCH CUR_GET_ADDSRVC INTO ln_disc;
    IF CUR_GET_ADDSRVC%NOTFOUND THEN
        ln_disc := 0;
    END IF;
    CLOSE CUR_GET_ADDSRVC;
    RETURN (ln_disc);
END;
------------------
FUNCTION FUN_FTCH_ADD_SRVC_DISC(guia_no VARCHAR2, srvc_id VARCHAR2) RETURN VARCHAR2 IS
  ln_disc BOK_GUIA_SRVC.gs_disc%TYPE;
    CURSOR CUR_GET_ADDSRVC IS
        SELECT GS_DISC
        FROM BOK_GUIA_SRVC
         WHERE GS_GUIA_NO = guia_no AND
         GS_SRVC_ID = SRVC_ID;
BEGIN
    OPEN CUR_GET_ADDSRVC;
    FETCH CUR_GET_ADDSRVC INTO ln_disc;
    IF CUR_GET_ADDSRVC%NOTFOUND THEN
        ln_disc := 0;
    END IF;
    CLOSE CUR_GET_ADDSRVC;
    RETURN (ln_disc);
END;
--------
/*Web Guia Cancellation*/
------------------
  /* This procedure should be called either by passing the origin branch id and
  form number or guia number. When passing the origin branch id and form number, pass null
  to guia number and while passing guia number, pass null to origin branch id and form
  number.  */
    PROCEDURE pro_ftch_guia_for_canc (orgn_clnt_id VARCHAR2, orgn_brnc_id IN OUT VARCHAR2, form_no IN OUT VARCHAR2,
        guia_no IN OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT VARCHAR2, guia_amnt OUT NUMBER, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2,site_name OUT VARCHAR2)
        IS
        q VARCHAR2(1):='Q';
         h VARCHAR2(1):='H';
         G_OR_F VARCHAR2(1);
        site_id BOK_GUIA_HEAD.GH_DEST_SITE_ID%TYPE;
         lc_actv_flag BOK_GUIA_HEAD.gh_actv_flag%TYPE;
      lc_dest_brnc_id     BOK_GUIA_HEAD.GH_DEST_BRNC_ID%TYPE;
      lc_orgn_brnc_id     BOK_GUIA_HEAD.GH_ORGN_BRNC_ID%TYPE;
      lc_orgn_clnt_id     BOK_GUIA_HEAD.GH_ORGN_CLNT_ID%TYPE;
      lc_guia_in_mnft   BOK_GUIA_HEAD.GH_GUIA_NO%TYPE;
      lc_guia_refr_no         BOK_GUIA_HEAD.GH_GUIA_REFR_NO%TYPE;    -- added by Hari on 25-03-2003
        CURSOR cur_ftch_form_no IS
          SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
              GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
              GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID
            FROM BOK_GUIA_HEAD
            WHERE GH_FORM_NO= FORM_NO    AND
            GH_PREP_BRNC_ID = orgn_brnc_id AND
            GH_DOCU_TYPE = q;
        CURSOR cur_ftch_guia_no IS
          SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
              GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
                GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID
            FROM BOK_GUIA_HEAD
            WHERE GH_GUIA_NO= GUIA_NO
            AND GH_GUIA_TYPE = h;
        /* CURSOR cur_guia_in_mnft(guia_no VARCHAR2) IS
            SELECT SM_GUIA_NO
            FROM web_shpr_mnft
            WHERE SM_GUIA_NO=guia_no; */ -- commented by Hari on 25-03-2003
    BEGIN
        --IF orgn_brnc_id IS NOT NULL AND form_no IS NOT NULL THEN
        --100% pass the origin branch id for both, ie for form no and for guia
        IF form_no IS NOT NULL THEN
            G_OR_F:='F';
          OPEN cur_ftch_form_no;
          FETCH cur_ftch_form_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
              lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id;
          CLOSE cur_ftch_form_no;
        ELSE
            G_OR_F:='G';
          OPEN cur_ftch_guia_no;
            FETCH cur_ftch_guia_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
              lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id;
          CLOSE cur_ftch_guia_no;
        END IF;
        IF lc_actv_flag ='A' THEN --This will either have 'A' - Active or 'I' - Converted to Guia
            /* OPEN cur_guia_in_mnft(guia_no);
            FETCH cur_guia_in_mnft INTO    lc_guia_in_mnft;
            CLOSE cur_guia_in_mnft; */ -- commented by Hari on 25-03-2003
            IF lc_orgn_clnt_id<>orgn_clnt_id THEN
                PRO_SHOW_MESG('WEB',language_id,800003,1,NULL,NULL,NULL,mesg_type,mesg_text);
            ELSIF lc_orgn_brnc_id<>orgn_brnc_id THEN
                PRO_SHOW_MESG('WEB',language_id,800004,1,NULL,NULL,NULL,mesg_type,mesg_text);
            /* ELSIF lc_guia_in_mnft IS NOT NULL THEN */ -- commented by Hari on 25-03-2003
            ELSIF lc_guia_refr_no IS NOT NULL THEN
                PRO_SHOW_MESG('WEB',language_id,800005,1,NULL,NULL,NULL,mesg_type,mesg_text);
            ELSE
                IF lc_dest_brnc_id IS NOT NULL THEN --Record found
                  brnc_name:= Pack_Web.fun_ftch_brnc_name(lc_dest_brnc_id);
                END IF;
                IF site_id IS NOT NULL THEN --Record found
                  site_name:= Pack_Web.fun_ftch_site_name(site_id);
                END IF;
            END IF;
        ELSIF lc_actv_flag ='I' THEN
            --PRO_SHOW_MESG('WEB',language_id,800002,1,NULL,NULL,NULL,mesg_type,mesg_text);
            PRO_SHOW_MESG('WEB','ESP',9900002,1,NULL,NULL,NULL,mesg_type,mesg_text);
        ELSE
            IF G_OR_F='F' THEN
                PRO_SHOW_MESG('BOK',language_id,900130,1,form_no,NULL,NULL,mesg_type,mesg_text);
            ELSE
                PRO_SHOW_MESG('BOK',language_id,900140,1,guia_no,NULL,NULL,mesg_type,mesg_text);
            END IF;

        END IF;

    END;
------------------
PROCEDURE pro_ftch_guia_for_canc_DE (orgn_clnt_id VARCHAR2, orgn_brnc_id IN OUT VARCHAR2, form_no IN OUT VARCHAR2,
    guia_no IN OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2, isse_date OUT VARCHAR2, guia_amnt OUT NUMBER,
    mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2,site_name OUT VARCHAR2, docu_type OUT VARCHAR2, mesg_id OUT NUMBER)
IS
   q VARCHAR2(1):='Q';
   h VARCHAR2(1):='H';
   p VARCHAR2(1):='P';
   n varchar2(1):='N';
   a varchar2(1):='A';
   G_OR_F VARCHAR2(1);
   site_id BOK_GUIA_HEAD.GH_DEST_SITE_ID%TYPE;
   lc_actv_flag BOK_GUIA_HEAD.gh_actv_flag%TYPE;
   lc_dest_brnc_id     BOK_GUIA_HEAD.GH_DEST_BRNC_ID%TYPE;
   lc_orgn_brnc_id     BOK_GUIA_HEAD.GH_ORGN_BRNC_ID%TYPE;
   lc_orgn_clnt_id     BOK_GUIA_HEAD.GH_ORGN_CLNT_ID%TYPE;
   lc_guia_in_mnft   BOK_GUIA_HEAD.GH_GUIA_NO%TYPE;
   lc_guia_refr_no    BOK_GUIA_HEAD.GH_GUIA_REFR_NO%TYPE;    -- added by Hari on 25-03-2003
   --lc_docu_type   BOK_GUIA_HEAD.GH_DOCU_TYPE%TYPE;
   lc_cliente_convenio VARCHAR2(15);
   lc_clnt_miembro VARCHAR(15);
   lc_clnt_miembro_site VARCHAR(5);
   lc_guia_N_activa number(1);

   CURSOR cur_ftch_form_no IS
   SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
   GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
   GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID, GH_DOCU_TYPE
   FROM BOK_GUIA_HEAD
   WHERE GH_FORM_NO= FORM_NO    AND
   GH_PREP_BRNC_ID = orgn_brnc_id AND
   GH_DOCU_TYPE IN (q,p);

   CURSOR cur_ftch_guia_no IS
   SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
   GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
   GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID, GH_DOCU_TYPE
   FROM BOK_GUIA_HEAD
   WHERE GH_GUIA_NO= GUIA_NO
   AND GH_GUIA_TYPE = h;

   CURSOR cur_list_of_members IS
    SELECT WC_MBR_ID, WC_SITE_ID
    FROM WEB_CLNT_MBR
    WHERE WC_CLNT_ID = lc_cliente_convenio;

        /* CURSOR cur_guia_in_mnft(guia_no VARCHAR2) IS
            SELECT SM_GUIA_NO
            FROM web_shpr_mnft
            WHERE SM_GUIA_NO=guia_no; */ -- commented by Hari on 25-03-2003

BEGIN
   --IF orgn_brnc_id IS NOT NULL AND form_no IS NOT NULL THEN
   --100% pass the origin branch id for both, ie for form no and for guia
   IF form_no IS NOT NULL THEN
      G_OR_F:='F';
      OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
      lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id, docu_type;
      CLOSE cur_ftch_form_no;
   ELSE
      G_OR_F:='G';
      OPEN cur_ftch_guia_no;
       FETCH cur_ftch_guia_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
       lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id, docu_type;
       CLOSE cur_ftch_guia_no;
   END IF;

   IF lc_actv_flag ='A' THEN --This will either have 'A' - Active or 'I' - Converted to Guia
      /* OPEN cur_guia_in_mnft(guia_no);
     FETCH cur_guia_in_mnft INTO    lc_guia_in_mnft;
     CLOSE cur_guia_in_mnft; */ -- commented by Hari on 25-03-2003
      IF lc_orgn_clnt_id<>orgn_clnt_id THEN
         SELECT NVL(WC_CLNT_ID,'') INTO lc_cliente_convenio
         FROM WEB_CLNT_MBR
         WHERE WC_MBR_ID =  orgn_clnt_id;

         OPEN cur_list_of_members;
         LOOP
            FETCH cur_list_of_members INTO lc_clnt_miembro, lc_clnt_miembro_site;
            IF lc_clnt_miembro = lc_orgn_clnt_id THEN
                IF lc_clnt_miembro_site <> SUBSTR(orgn_brnc_id,0,3) THEN
                    --Aqui va el mensaje: La guia debe ser cancelada en tal plaza: lc_clnt_miembro_site
                    mesg_id := 800062;
                    PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
                    mesg_text:= mesg_text || ' ' || fun_ftch_site_name(lc_clnt_miembro_site);
                    EXIT;--Salimos del ciclo cuando encontremos
                END IF;
            ELSE
                --Entro aqu¡ por que la guia no pertenece a otro cliente convenio
                mesg_id := 800003;
                PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
            END IF;
            EXIT WHEN cur_list_of_members%notfound;
        END LOOP;
        CLOSE cur_list_of_members;

      ELSIF SUBSTR(lc_orgn_brnc_id,0,3)<>SUBSTR(orgn_brnc_id,0,3) THEN
         mesg_id := 800004;
         PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
         /* ELSIF lc_guia_in_mnft IS NOT NULL THEN */ -- commented by Hari on 25-03-2003
      ELSIF lc_guia_refr_no IS NOT NULL THEN
         mesg_id := 800005;
         PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
      ELSE
         IF lc_dest_brnc_id IS NOT NULL THEN --Record found
            brnc_name:= Pack_Web.fun_ftch_brnc_name(lc_dest_brnc_id);
         END IF;
         IF site_id IS NOT NULL THEN --Record found
            site_name:= Pack_Web.fun_ftch_site_name(site_id);
         END IF;
      END IF;
   ELSIF lc_actv_flag ='I' THEN
      lc_guia_N_activa := 0;
      SELECT COUNT(GH_GUIA_NO) INTO lc_guia_N_activa
      FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = guia_no AND GH_GUIA_TYPE = n AND GH_ACTV_FLAG = a;
      IF lc_guia_N_activa > 0 THEN
        mesg_id := 800063; --Significa que la guia H esta inactiva, pero existe una guia N activa
      ELSE
        mesg_id := 9900002; -- Significa que tanto la guia H o N (si hubiera) estan inactivas
      END IF;
      --PRO_SHOW_MESG('WEB',language_id,800002,1,NULL,NULL,NULL,mesg_type,mesg_text);
      PRO_SHOW_MESG('WEB','ESP',mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
   ELSE
      IF G_OR_F='F' THEN
         mesg_id := 900130;
         PRO_SHOW_MESG('BOK',language_id,mesg_id,1,form_no,NULL,NULL,mesg_type,mesg_text);
      ELSE
         mesg_id := 900140;
         PRO_SHOW_MESG('BOK',language_id,mesg_id,1,guia_no,NULL,NULL,mesg_type,mesg_text);
       END IF;

   END IF;

END;
------------------
    PROCEDURE pro_del_guia(guia_no VARCHAR2, mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2) IS
        lv_docu    BOK_GUIA_HEAD.gh_docu_type%TYPE :='Q';
        lv_type    BOK_GUIA_HEAD.gh_guia_type%TYPE :='H';
    BEGIN
        SAVEPOINT S1;
        DELETE FROM BOK_GUIA_SRVC
            WHERE gs_guia_no = guia_no
            AND gs_docu_type = lv_docu
            AND gs_guia_type = lv_type ;
        DELETE FROM BOK_GUIA_SRVC_ITEM
            WHERE gl_guia_no = guia_no
            AND gl_docu_type = lv_docu
            AND gl_guia_type = lv_type;
        DELETE FROM BOK_GUIA_SRVC_ITEM_REQM
            WHERE gi_guia_no = guia_no;
        DELETE FROM BOK_GUIA_STUS
            WHERE gs_guia_no = guia_no;
        DELETE FROM BOK_GUIA_ADDR
            WHERE ga_guia_no = guia_no;
        DELETE FROM BOK_GUIA_HEAD
            WHERE gh_guia_no = guia_no
            AND gh_guia_type = lv_type
            AND gh_docu_type = lv_docu;
        COMMIT;
        PRO_SHOW_MESG('BOK',LANGUAGE_ID,900177,1,NULL,NULL,NULL,mesg_type,mesg_text);
    EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK TO S1;
                mesg_type:=NULL;
                mesg_text:=SQLERRM;
    END;
/*------------------
    PROCEDURE pro_ftch_detl_for_form_no(orgn_brnc_id IN VARCHAR2, form_no IN VARCHAR2,
        guia_no OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT DATE, guia_amnt OUT NUMBER, actv_flag OUT VARCHAR2) IS
      q VARCHAR2(1):='Q';
      lc_dest_brnc_id     bok_guia_head.GH_DEST_CLNT_ID%TYPE;
        CURSOR cur_ftch_form_no IS
          SELECT GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, GH_ISSE_DATE, GH_GUIA_AMNT, GH_ACTV_FLAG
            FROM BOK_GUIA_HEAD
            WHERE GH_FORM_NO= FORM_NO    AND
            GH_PREP_BRNC_ID = orgn_brnc_id AND
            GH_DOCU_TYPE =q;
    BEGIN
      OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO guia_no, lc_dest_brnc_id, dest_clnt_name, isse_date, guia_amnt, actv_flag;
        CLOSE cur_ftch_form_no;
      IF lc_dest_brnc_id IS NOT NULL THEN
          brnc_name:= pack_web.fun_ftch_brnc_name(lc_dest_brnc_id);
      END IF;
      NULL;
    END;
------------------
    PROCEDURE pro_ftch_detl_for_guia_no(guia_no IN VARCHAR2, orgn_brnc_id OUT VARCHAR2,
        form_no OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2,
        isse_date OUT DATE, guia_amnt OUT NUMBER, actv_flag OUT VARCHAR2) IS
      h VARCHAR2(1):='H';
      lc_dest_brnc_id     bok_guia_head.GH_DEST_CLNT_ID%TYPE;
         CURSOR cur_ftch_guia_no IS
          SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, GH_ISSE_DATE,
          GH_GUIA_AMNT, GH_ACTV_FLAG
            FROM BOK_GUIA_HEAD
            WHERE GH_GUIA_NO= GUIA_NO    AND
            --GH_PREP_BRNC_ID = orgn_brnc_id AND
            GH_GUIA_TYPE =h;
    BEGIN
      OPEN cur_ftch_guia_no;
      FETCH cur_ftch_guia_no INTO orgn_brnc_id, form_no, lc_dest_brnc_id, dest_clnt_name,
          isse_date, guia_amnt, actv_flag;
        CLOSE cur_ftch_guia_no;
      IF lc_dest_brnc_id IS NOT NULL THEN
          brnc_name:= pack_web.fun_ftch_brnc_name(lc_dest_brnc_id);
      END IF;
      NULL;
    END;*/

/* Function that fetches the tariff amt for shp srvc and addl srvc from BOK_GUIA_SRVC */

    FUNCTION fun_calc_shp_totl(guia_no VARCHAR2)
    RETURN NUMBER IS
    shp_amt NUMBER;
    BEGIN
    SELECT SUM((gs_sub_totl - gs_disc + gs_tax - gs_tax_ret)) INTO shp_amt
    FROM BOK_GUIA_SRVC
    WHERE gs_srvc_id IN ('SHP-G','SHP-E')
    AND gs_guia_no = guia_no
    AND gs_docu_type IN ('Q','G')
    AND gs_guia_type IN ('H','N');
    RETURN(shp_amt);
    END;

    FUNCTION fun_calc_add_totl(guia_no VARCHAR2)
    RETURN NUMBER IS
    add_amt NUMBER;
    BEGIN
    SELECT SUM((gs_sub_totl - gs_disc + gs_tax - gs_tax_ret)) INTO add_amt
    FROM BOK_GUIA_SRVC
    WHERE gs_srvc_id NOT IN ('SHP-G','SHP-E')
    AND gs_guia_no = guia_no
    AND gs_docu_type IN ('Q','G')
    AND gs_guia_type IN ('H','N');
    RETURN(add_amt);
    END;
--------------------------------------------------------------------------------------------
PROCEDURE Pro_Ftch_Destbrnc_Report(guia_refr_no VARCHAR2,tariff_tot OUT NUMBER,
                                                   ack_tot OUT NUMBER,rad_tot OUT NUMBER,
                                   ead_tot OUT NUMBER,ins_tot OUT NUMBER,cod_tot OUT NUMBER,
                                   addl_tot OUT NUMBER,other_addl_tot OUT NUMBER,
                                   dest_brnc_id VARCHAR2)IS
   CURSOR cur_mnft_guia_amt_dtl IS
      SELECT DISTINCT gh_guia_no gh_guia_no
        FROM  BOK_GUIA_HEAD
       WHERE gh_guia_refr_no = guia_refr_no
           AND gh_dest_brnc_id = dest_brnc_id;--added by B.Emerson on 05/06/2003
   v_tariff_tot     NUMBER;
   v_ack_tot        NUMBER;
   v_rad_tot        NUMBER;
   v_ead_tot        NUMBER;
   v_ins_tot        NUMBER;
   v_cod_tot        NUMBER;
   v_addl_tot       NUMBER;
   v_other_addl_tot NUMBER;

BEGIN
   FOR i IN cur_mnft_guia_amt_dtl LOOP
   /* Call Procedure PRO_CALC_TOTL against Individual Guia No for Summation of Amount for
   /* Tariff,Ack,Ead,Rad,Ins,Cod,Addition Service */
         PRO_CALC_TOTL(i.gh_guia_no,v_tariff_tot,v_ack_tot,v_rad_tot,v_ead_tot,
                       v_ins_tot,v_cod_tot,v_addl_tot,v_other_addl_tot);
       /* Following will capture the Summation of
       /* Tariff,Ack,Ead,Rad,Ins,Cod,Addition Service for all Guia's Under one manifest */
       tariff_tot := NVL(tariff_tot,0) + v_tariff_tot;
       ack_tot    := NVL(ack_tot,0) + v_ack_tot;
       rad_tot    := NVL(rad_tot,0) + v_rad_tot;
       ead_tot    := NVL(ead_tot,0) + v_ead_tot;
       ins_tot    := NVL(ins_tot,0) + v_ins_tot;
       cod_tot    := NVL(cod_tot,0) + v_cod_tot;
       addl_tot   := NVL(addl_tot,0) + v_addl_tot;
       other_addl_tot := NVL(other_addl_tot,0) + v_other_addl_tot;
       IF cur_mnft_guia_amt_dtl%NOTFOUND THEN
                   EXIT;
            END IF;
   END LOOP;
END;
--------------------------------------------------------------------------------------------
PROCEDURE PRO_CALC_TOTL (guia_no IN VARCHAR2, shp_totl OUT NUMBER, ack_totl OUT NUMBER,
                                       rad_totl OUT NUMBER, ead_totl OUT NUMBER, ins_totl OUT NUMBER,
                                       cod_totl OUT NUMBER, addl_totl OUT NUMBER,
                                       other_addl_totl OUT NUMBER)
    IS

    CURSOR Cur_srvc_amt IS
    SELECT NVL(SUM((NVL(gs_sub_totl,0) - NVL(GS_DISC,0) + NVL(GS_TAX,0) - NVL(GS_TAX_RET,0))),0) srvc_amt ,gs_srvc_id
    FROM  BOK_GUIA_SRVC
    WHERE gs_guia_no = guia_no
    AND gs_docu_type IN ('Q','G')
    AND gs_guia_type IN ('H','N')
GROUP BY gs_srvc_id;

    other_add_totl NUMBER := 0;
    ship_totl NUMBER := 0;
    v_shp_env NUMBER := 0;
  v_shp_pac NUMBER := 0;
  v_ack NUMBER     := 0;
  v_rad NUMBER     := 0;
  v_ins NUMBER     := 0;
  v_ead NUMBER     := 0;
  v_cod NUMBER     := 0;
  v_addl NUMBER    := 0;
    v_other_addl NUMBER := 0;

  BEGIN
      FOR i IN cur_srvc_amt
      LOOP
          IF i.gs_srvc_id IS NOT NULL THEN
           IF i.gs_srvc_id = 'SHP-E' THEN
            v_shp_env := v_shp_env + i.srvc_amt;
         ELSIF i.gs_srvc_id = 'SHP-G' THEN
            v_shp_pac := v_shp_pac + i.srvc_amt;
         END IF;
         shp_totl := v_shp_env + v_shp_pac; /* assigned to out parameter */
         IF i.gs_srvc_id = 'ACK'  THEN -- calc total for ACK srvc
              v_ack := i.srvc_amt;
         END IF;
         ack_totl := v_ack; /* assigned to out parameter */

           IF i.gs_srvc_id = 'RAD' THEN -- calc total for RAD srvc
              v_rad := i.srvc_amt;
           END IF;
           rad_totl := v_rad; /* assigned to out parameter */

            IF i.gs_srvc_id = 'EAD' THEN -- calc total for EAD srvc
              v_ead := i.srvc_amt;
           END IF;
         ead_totl := v_ead; /* assigned to out parameter */

         IF i.gs_srvc_id = 'INV' THEN -- calc total for INS srvc
               v_ins := i.srvc_amt;
         END IF;
         ins_totl := v_ins; /* assigned to out parameter */

         IF i.gs_srvc_id = 'COD' THEN -- calc total for COD srvc
              v_cod := i.srvc_amt;
         END IF;
         cod_totl := v_cod;     /* assigned to out parameter */

           IF i.gs_srvc_id NOT IN ('SHP-E','SHP-G','ACK','RAD','EAD','INV','COD') THEN -- calc total for Other Additional Service
              v_other_addl := v_other_addl + i.srvc_amt;
           END IF;
           other_addl_totl := v_other_addl; /* assigned to out parameter */
           v_addl := v_shp_env + v_shp_pac + v_ack + v_rad + v_ead + v_ins + v_cod + v_other_addl;  -- calc total for all the srvc (Shipment + Additional + other additional)

         addl_totl := v_addl; /* assigned to out parameter */
          END IF;
      END LOOP;
END;
--------------------------------------------------------------------------------------------
    -- Procedures and functions from PACK_GUIA_PRINT  - START
PROCEDURE PRO_GET_INV_MESG(lv_mesg1 IN OUT VARCHAR,var1 IN VARCHAR,
var2 IN VARCHAR,var3 IN VARCHAR)
IS

lv_mesg VARCHAR2(1000);
lv_mesg2 VARCHAR2(1000);

pos NUMBER;
cnt NUMBER := 0;
v1 VARCHAR2(100) := NVL(var1,' ');
v2 VARCHAR2(100) := NVL(var2,' ');
v3 VARCHAR2(100) := NVL(var3,' ');
amp VARCHAR2(80);
amp1 CHAR(2);
FLAG NUMBER(1) := 0;

BEGIN
   lv_mesg := lv_mesg1;

   /*positional substitution of '#' for special messages*/
   FOR i IN 1..3 LOOP
      pos := INSTR(lv_mesg1,'#',1,i);

      IF pos > 0 THEN
         cnt := cnt+1;
         amp := SUBSTR(lv_mesg1,pos,pos+2);
         amp1 := SUBSTR(amp,1,2);

         IF cnt = 1 THEN
            lv_mesg2 := REPLACE(lv_mesg1,amp1,v1);
            FLAG := 1;
         END IF;

         IF cnt = 2 THEN
            lv_mesg2 := REPLACE(lv_mesg2,amp1,v2);
            FLAG := 1;
         END IF;

         IF cnt = 3 THEN
            lv_mesg2 := REPLACE(lv_mesg2,amp1,v3);
            FLAG := 1;
         END IF;
         lv_mesg := lv_mesg2;
      END IF;

      IF FLAG <> 1 THEN
         lv_mesg := lv_mesg1;
      END IF;
   END LOOP;

   IF lv_mesg IS NOT NULL THEN  --added by sathish on 06-09-01
      lv_mesg1 := lv_mesg||';';
   ELSE
      lv_mesg1 := lv_mesg;
   END IF;
END;


    -- Procedures and functions from PACK_GUIA_PRINT  - END

--Procedure to delete the manifests that were excluded --
PROCEDURE Pro_Mnft_Del(manifestNumber IN VARCHAR2,clientid IN VARCHAR2)IS
lc_transNo VARCHAR2(15);
lc_maxSerlNo NUMBER;

CURSOR cur_ftch_guia(lc_manifestNumber VARCHAR2) IS
       SELECT gh_guia_no,GH_CURR_LOCA,GH_CURR_DEST
         FROM BOK_GUIA_HEAD
           WHERE gh_guia_type = 'H'
          AND gh_actv_flag = 'A'
          AND gh_guia_refr_no = lc_manifestNumber;

BEGIN
     SELECT CT_TRNS_NO INTO lc_transNo FROM COM_CTT
      WHERE CT_SECO_ENTY= manifestNumber
        AND CT_TRNS_TYPE = 'IIM'
        AND CT_PRIM_ENTY = clientid;

     UPDATE COM_CTT SET CT_DATE_03 = SYSDATE
      WHERE CT_SECO_ENTY= manifestNumber
        AND CT_TRNS_TYPE = 'IIM'
        AND CT_PRIM_ENTY = clientid
        AND CT_TRNS_NO   = lc_transno;

    --OPEN cur_ftch_guia(manifestNumber);
    FOR i IN cur_ftch_guia(manifestNumber) LOOP
        SELECT NVL(MAX(GS_SERL_NO),0)+ 1 INTO lc_maxSerlNo FROM BOK_GUIA_STUS
               WHERE gs_guia_no = i.gh_guia_no;
        INSERT INTO BOK_GUIA_STUS
                (GS_GUIA_NO,GS_STUS_CODE,GS_SERL_NO,GS_COM_TRNS_REF_CODE,
                 CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,GS_CTT_TRNS_TYPE,
                 GS_CURR_LOCA,GS_CURR_DEST)
               VALUES(i.gh_guia_no,'RFM',lc_maxSerlNo,lc_transNo,
                           SYSDATE,clientid,SYSDATE,clientid,'RFM',
                      I.GH_CURR_LOCA,i.GH_CURR_DEST);

        UPDATE BOK_GUIA_HEAD SET gh_guia_stus = 'RFM',gh_guia_refr_no = NULL
               WHERE gh_guia_no = i.gh_guia_no
               AND gh_guia_type = 'H'
               AND gh_actv_flag = 'A';
    END LOOP;
    --CLOSE cur_ftch_guia;
    DELETE FROM WEB_MNFT_DETL WHERE MD_MNFT_NO = manifestNumber;
    COMMIT;


END;
    --END of Procedure to delete the manifests that were excluded --
/* Display  City from current user's Branch default address*/


PROCEDURE pro_get_city (entity_id in VARCHAR2, city out VARCHAR2, type4 out VARCHAR2, levl4 out VARCHAR2, code4 out VARCHAR2, ref_no OUT varchar2)

IS
   CURSOR cur_addr (lv_bran VARCHAR2, lv_y VARCHAR2)
   IS
      SELECT am_gety_levl, am_gety_type, am_gety_code, am_addr_ref_no
        FROM sys_addr_mstr
       WHERE am_addr_type = lv_bran
         AND am_defa_flag = lv_y
         AND am_enty_id = UPPER (entity_id);

   lc_levl     sys_addr_mstr.am_gety_levl%TYPE;
   lc_type     sys_addr_mstr.am_gety_type%TYPE;
   lc_code     sys_addr_mstr.am_gety_code%TYPE;
--   lc_ref_no   sys_addr_mstr.am_addr_ref_no%TYPE;
   lc_city     sys_cust_addr.ca_levl_4%TYPE;
   msgnum      NUMBER;
   mess        NUMBER;
BEGIN
   --Get Geoentity level,type and code from Address Master
   OPEN cur_addr ('BRAN', 'Y');

   FETCH cur_addr
    INTO lc_levl, lc_type, lc_code, ref_no;

   IF lc_levl IS NULL AND ref_no IS NOT NULL
   THEN
      DECLARE
         CURSOR cur_cust_addr
         IS
            SELECT ca_levl_4
              FROM sys_cust_addr, sys_addr_mstr
             WHERE ca_addr_id = am_addr_ref_no;
      BEGIN
         -- Get City from Custom Address Table
         OPEN cur_cust_addr;

         FETCH cur_cust_addr
          INTO lc_city;

         city := lc_city;

         IF cur_cust_addr%NOTFOUND
         THEN
            city := NULL;
         END IF;

         CLOSE cur_cust_addr;
      END;
   -- Get City from Geo entity master Table
   ELSIF lc_levl IS NOT NULL AND ref_no IS NULL
   THEN
       pro_get_all (lc_levl, lc_type, lc_code, city, type4, levl4, code4);
   END IF;

   IF cur_addr%NOTFOUND
   THEN
      city := NULL;
      type4 := NULL;
      levl4 := NULL;
      code4 := NULL;
   END IF;

   CLOSE cur_addr;

END pro_get_city;


PROCEDURE PRO_GET_ALL(levl_gr VARCHAR2,type_gr VARCHAR2, code_gr VARCHAR2, gdesc OUT VARCHAR2, gtype OUT VARCHAR2, glevl OUT VARCHAR2, gcode OUT VARCHAR2) IS

   ln_lr  VARCHAR2(15);
   ln_tr  VARCHAR2(15);
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(60);
   MSGNUM NUMBER;
   ln_dummy NUMBER;


BEGIN
   SELECT gr_levl_r,gr_type_r,gr_code_r,ge_desc
   INTO ln_lr,ln_tr,lc_mcd,lc_gdesc
   FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
   WHERE GR_CODE = UPPER(code_gr)
   AND gr_levl =levl_gr
   AND gr_type =type_gr
   AND GM.GE_LEVL=GR.GR_LEVL_R AND GM.GE_TYPE=GR.GR_TYPE_R
   AND GM.GE_CODE=GR.GR_CODE_R;

  LOOP
       SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC
     INTO ln_lr,ln_tr,lc_mcd,lc_gdesc FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
     WHERE GR_LEVL=ln_lr AND GR_TYPE=ln_tr
      AND GR_CODE=lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R
      AND GM.GE_CODE=GR.GR_CODE_R;

   --Display City
   --IF ln_lr= 4 THEN--AAP06
   IF ln_lr= 5 THEN--AAP06
       gtype := ln_tr;
       glevl := ln_lr;
       gcode := lc_mcd;
       gdesc := lc_gdesc;
       --ln_dummy :=4;--AAP06
       ln_dummy :=5;--AAP06
   END IF;

      --IF LN_LR=4 THEN--AAP06
      IF LN_LR=5 THEN--AAP06
            EXIT;
      END IF;
   END LOOP;


   EXCEPTION
           WHEN NO_DATA_FOUND THEN
               lc_gdesc := null;
           WHEN OTHERS THEN
               lc_gdesc := null;
END PRO_GET_ALL;


PROCEDURE pro_get_geoenty_postal(lc_mpc IN VARCHAR2, levl1 OUT VARCHAR2,levl2 OUT VARCHAR2,
levl3 OUT VARCHAR2,levl4 OUT VARCHAR2,levl5 OUT VARCHAR2,levl6 OUT VARCHAR2,levl7 OUT VARCHAR2,
levc1 OUT VARCHAR2, levc2 OUT VARCHAR2, levc3 OUT VARCHAR2,levc4 OUT VARCHAR2,levc5 OUT VARCHAR2,
levc6 OUT VARCHAR2,zipcode OUT VARCHAR2, lc_lvl IN VARCHAR2, lc_typ IN VARCHAR2) IS
   ln_lr  NUMBER;
   ln_tr  NUMBER;
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(500);
   lc_gdesc1 VARCHAR2(5000);
   lc_zipcode VARCHAR2(100);
   MSGNUM NUMBER;
   CURSOR pro_gety_c1(lv_lc_mpc VARCHAR2, lv_lc_lvl VARCHAR2, lv_lc_typ VARCHAR2) IS
           SELECT GE_DESC,GE_CODE,ZIP_CODE FROM SYS_GETY_MSTR WHERE GE_CODE=LV_LC_MPC
        AND GE_LEVL = lv_lc_lvl AND GE_TYPE = lv_lc_typ;
   CURSOR pro_gety_c2(lv_lc_mpc VARCHAR2, lv_lc_lvl VARCHAR2, lv_lc_typ VARCHAR2) IS
      SELECT DECODE(GR_LEVL_R,6,6,5,66,NULL),GR_TYPE_R,GR_CODE_R,GE_DESC,ZIP_CODE
         FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
         WHERE GR_CODE = lv_lc_mpc
        AND GR_LEVL = lv_lc_lvl AND GR_TYPE = lv_lc_typ
        AND GM.GE_LEVL=GR.GR_LEVL_R    AND GM.GE_TYPE=GR.GR_TYPE_R
        AND GM.GE_CODE=GR.GR_CODE_R;
   CURSOR pro_gety_c3(lv_ln_lr NUMBER,lv_ln_tr NUMBER,lv_lc_mcd VARCHAR2) IS
           SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC,ZIP_CODE
         FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
         WHERE GR_LEVL=lv_ln_lr AND GR_TYPE=lv_ln_tr
      AND GR_CODE=lv_lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R AND GM.GE_CODE=GR.GR_CODE_R;
BEGIN
    BEGIN

        OPEN pro_gety_c1(lc_mpc, lc_lvl, lc_typ);
      FETCH pro_gety_c1 INTO LEVL6,LEVC6,zipcode;
      IF pro_gety_c1%NOTFOUND THEN
           NULL;
      END IF;
      CLOSE pro_gety_c1;
    END;
        OPEN pro_gety_c2(lc_mpc, lc_lvl, lc_typ);
      FETCH pro_gety_c2 INTO ln_lr,ln_tr,lc_mcd,lc_gdesc,lc_zipcode;
      IF pro_gety_c2%NOTFOUND THEN
          lc_gdesc1 := NULL;
      END IF;
      CLOSE pro_gety_c2;
    IF ln_lr=66 THEN
         levl5 := UPPER(lc_gdesc);
         ln_lr := 5;
         levc5 := lc_mcd;
    END IF;
   LOOP
           OPEN pro_gety_c3(ln_lr,ln_tr,lc_mcd);
           FETCH pro_gety_c3 INTO ln_lr,ln_tr,lc_mcd,lc_gdesc,lc_zipcode;
           IF pro_gety_c3%NOTFOUND THEN
               NULL;
           END IF;
           CLOSE pro_gety_c3;
           lc_gdesc1 :=lc_gdesc1||', '||RTRIM(lc_gdesc) ;
           IF ln_lr = 1 THEN
                levl1 := UPPER(lc_gdesc);
                levc1 := lc_mcd;
           END IF;
           IF ln_lr = 2 THEN
                levl2 := UPPER(lc_gdesc);
                levc2 := lc_mcd;
           END IF;
           IF ln_lr = 3 THEN
                levl3 := UPPER(lc_gdesc);
                levc3 := lc_mcd;
           END IF;
           IF ln_lr = 4 THEN
                levl4 := UPPER(lc_gdesc);
                levc4 := lc_mcd;
           END IF;
           IF ln_lr = 5 THEN
                levl5 := UPPER(lc_gdesc);
                levc5 := lc_mcd;
           END IF;
           IF LN_LR=1 AND LN_TR=1 THEN
               EXIT;
           END IF;
END LOOP;
    EXCEPTION
         WHEN NO_DATA_FOUND THEN
               lc_gdesc := null;
           WHEN OTHERS THEN
               lc_gdesc := null;

END PRO_GET_GEOENTY_postal;

PROCEDURE pro_ftch_unst_postal(ref_no VARCHAR2,P1 OUT VARCHAR2,p2 OUT VARCHAR2,
    p3 OUT VARCHAR2,p4 OUT VARCHAR2,p5 OUT VARCHAR2,p6 OUT VARCHAR2,p7 OUT VARCHAR2) IS
    CURSOR cur_ftch_unst(lv_ref_no VARCHAR2) IS
    SELECT CA_LEVL_1, CA_LEVL_2, CA_LEVL_3, CA_LEVL_4, CA_LEVL_5, CA_LEVL_6, CA_LEVL_7
    FROM SYS_CUST_ADDR WHERE CA_ADDR_ID=lv_ref_no;
BEGIN
    OPEN cur_ftch_unst(ref_no);
    FETCH cur_ftch_unst INTO P1,p2,p3,p4,p5,p6,p7;
    IF cur_ftch_unst%NOTFOUND THEN
        P1 := '';
        p2 := '';
        p3 := '';
        p4 := '';
        p5 := '';
        p6 := '';
        p7 := '';
    END IF;
    CLOSE cur_ftch_unst;
END pro_ftch_unst_postal;

PROCEDURE pro_ftch_addr_postal(am_defn_type VARCHAR2, am_ref_no VARCHAR2, am_gcode VARCHAR2,
    am_glevl NUMBER, am_gtype NUMBER,
     u11 OUT VARCHAR2,u12 OUT VARCHAR2,u13 OUT VARCHAR2,
   u14 OUT VARCHAR2,u15 OUT VARCHAR2,u16 OUT VARCHAR2,u17 OUT VARCHAR2,zipcode OUT VARCHAR2,
   c11 OUT VARCHAR2,c12 OUT VARCHAR2,c13 OUT VARCHAR2,c14 OUT VARCHAR2,c15 OUT VARCHAR2,c16 OUT VARCHAR2) IS
    u1 VARCHAR2(50);u2 VARCHAR2(50);
    u3 VARCHAR2(50);u4 VARCHAR2(50);u5 VARCHAR2(50);
    u6 VARCHAR2(60);u7 VARCHAR2(50);c1 VARCHAR2(50);
    c2 VARCHAR2(50);c3 VARCHAR2(50);c4 VARCHAR2(50);
    c5 VARCHAR2(50);c6 VARCHAR2(50);

BEGIN
          IF am_defn_type='N' THEN
              pro_ftch_unst_postal(am_ref_no,u1,u2,u3,u4,u5,u6,u7);
              u11 := u1;  u12 := u2;  u13 := u3;  u14 := u4;
              u15 := u5;  u16 := u6;  u17 := u7;zipcode := u7;
              c11 := NULL;c12:= NULL; c13 :=NULL;c14 := NULL;
              c15 := NULL;c16:= NULL;
          ELSE
              pro_get_geoenty_postal(am_gcode,u1,u2,u3,u4,u5,u6,u7,c1,c2,c3,c4,c5,c6,zipcode, am_glevl, am_gtype); -- parameters level and type added on 13-03-2003 by Hari
              u11 := u1;  u12 := u2;  u13 := u3;  u14 := u4;
              u15 := u5;  u16 := u6;  u17 := u7;
              c11 := c1;  c12 := c2;  c13 := c3;  c14 := c4;
              c15 := c5;  c16 := c6;
          END IF;
END pro_ftch_addr_postal;
PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,
dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,
brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,
eadcheck out varchar2, lc_levl out VARCHAR2,
   lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,
   POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2,
--   lc_custClntId OUT VARCHAR2,lc_email OUT VARCHAR2,clientPQId VARCHAR2,lc_clientId out VARCHAR2) IS
   lc_email OUT VARCHAR2,lc_clientId out VARCHAR2) IS

   ln_lr  VARCHAR2(15);
   ln_tr  VARCHAR2(15);
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(60);
   lc_gdesc4 VARCHAR2(500);
   lc_gdesc3 VARCHAR2(500);
   lc_gdesc2 VARCHAR2(500);
   MSGNUM NUMBER;
   ln_dummy NUMBER;
   city varchar2(100);
   addrefno varchar2(100);
   site_id varchar2(100);
   AM_ADDR_CODE varchar2(100);
   lc_clnt_id VARCHAR2(15);
   lc_temp VARCHAR2(15) := clientid;

BEGIN

       BEGIN
    SELECT cm_clnt_name, cm_ead_flag
      INTO clnt_name, eadcheck
      FROM sys_clnt_mstr
     WHERE cm_clnt_id = clientid and CM_ACTV_FLAG ='A';--AAP03
            -- SELECT cm_clnt_name,cm_ead_flag,CM_CUST_CLNT_ID into clnt_name,eadcheck,lc_custClntId from sys_clnt_mstr where cm_clnt_id=clientId;
          EXCEPTION
         WHEN OTHERS THEN
            clnt_name:=NULL;
       END;


    BEGIN

       IF lc_temp is NULL THEN

          SELECT am_addr_code, am_pe_site_id, am_phno1, am_strt_name, am_drnr,
                 am_gety_levl, am_gety_type, am_gety_code, am_mail_id
           INTO  am_addr_code, site_id, phno1, strt_name, drnr,
                 lc_levl, lc_type, lc_code, lc_email
          FROM sys_clnt_mstr, sys_addr_mstr
          WHERE CM_CLNT_ID = am_enty_id
          and am_defa_flag = 'Y' AND am_addr_type = 'CLNT';

          --SELECT CM_CLNT_ID into lc_clientId from sys_clnt_mstr where cm_cust_clnt_id = clientpqid;

       else
          SELECT am_addr_code, am_pe_site_id, am_phno1, am_strt_name, am_drnr,
           am_gety_levl, am_gety_type, am_gety_code, am_mail_id
          INTO am_addr_code, site_id, phno1, strt_name, drnr,
          lc_levl, lc_type, lc_code, lc_email
          FROM sys_addr_mstr
          WHERE am_enty_id = clientid AND am_defa_flag = 'Y' AND am_addr_type = 'CLNT';
        end if;


       EXCEPTION
         WHEN OTHERS THEN
            brnc_id:=NULL;
    end;

    BEGIN
    pack_web.PRO_ASSIGN_DEST_BRNC(lc_code,lc_type,lc_levl,brnc_id,BRNC_NAME,site_id);

      --   select BM_BRNC_NAME into BRNC_NAME from sys_brnc_mstr where BM_BRNC_ID=brnc_id;
       EXCEPTION
       WHEN OTHERS THEN
        BRNC_NAME:=NULL;
    end;


        pro_get_city(brnc_id,city,POSTALTYPE,POSTALLEVEL,POSTALCODE,addrefno);

      BEGIN
       SELECT ZIP_CODE INTO POSTAL
           FROM SYS_GETY_MSTR
           WHERE    GE_LEVL=lc_levl AND
             GE_TYPE=lc_type AND
               GE_CODE = lc_code;
      EXCEPTION
     WHEN OTHERS THEN
        POSTAL:=NULL;
      END;


    SELECT GE_DESC into dis_col  FROM SYS_GETY_MSTR
    WHERE ge_levl = upper(lc_levl)
    AND ge_type = UPPER(lc_type)
    AND ge_code = upper(lc_code);

   SELECT gr_levl_r,gr_type_r,gr_code_r,ge_desc
   INTO ln_lr,ln_tr,lc_mcd,lc_gdesc
   FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
   WHERE GR_CODE = UPPER(lc_code)
   AND gr_levl =lc_levl
   AND gr_type =lc_type
   AND GM.GE_LEVL=GR.GR_LEVL_R AND GM.GE_TYPE=GR.GR_TYPE_R
   AND GM.GE_CODE=GR.GR_CODE_R;
    --Display Colonia
     IF ln_lr= 6 THEN -- AND ln_tr = 1
         dis_col:= lc_gdesc ;
     END IF;
    --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
        --de_po_pais:= lc_gdesc ;--AAP06
        dis_city := lc_gdesc;--AAP06
     END IF;
  LOOP
     SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC
     INTO ln_lr,ln_tr,lc_mcd,lc_gdesc FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
     WHERE GR_LEVL=ln_lr AND GR_TYPE=ln_tr
      AND GR_CODE=lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R
      AND GM.GE_CODE=GR.GR_CODE_R;

   --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
         --de_po_pais:= lc_gdesc ;--AAP06
         dis_city := lc_gdesc;--AAP06
     END IF;
   --Display City
    IF ln_lr= 4 THEN --AND ln_tr = 1
       --dis_city := lc_gdesc;--AAP06
       de_po_pais:= lc_gdesc ;--AAP06
    END IF;
   -- Display Muncipio
    IF ln_lr =3 THEN --AND ln_tr =1
        di_muncipo := lc_gdesc;
    end if;
   -- Display State
    IF ln_lr = 2 THEN -- AND ln_tr = 1
         dis_state := lc_gdesc;
    end if;
   -- Display Country
    IF ln_lr = 1 THEN --AND ln_tr = 1
         dis_con := lc_gdesc;
    end if;
    IF LN_LR = 1 THEN --AND LN_TR = 1
    EXIT;
    END IF;
   END LOOP;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
       lc_gdesc := null;
    WHEN OTHERS THEN
       lc_gdesc := null;
END;
----------------------
--comentado el 26/Abril/2012 para poner nueva version sin utilizar el numero personalizado en sys_clnt_mstr
/*PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,
dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,
brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,
eadcheck out varchar2, lc_levl out VARCHAR2,
   lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,
   POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2,
   lc_custClntId OUT VARCHAR2,lc_email OUT VARCHAR2,clientPQId VARCHAR2,lc_clientId out VARCHAR2) IS


   ln_lr  VARCHAR2(15);
   ln_tr  VARCHAR2(15);
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(60);
   lc_gdesc4 VARCHAR2(500);
   lc_gdesc3 VARCHAR2(500);
   lc_gdesc2 VARCHAR2(500);
   MSGNUM NUMBER;
   ln_dummy NUMBER;
   city varchar2(100);
   addrefno varchar2(100);
   site_id varchar2(100);
   AM_ADDR_CODE varchar2(100);
   lc_clnt_id VARCHAR2(15);
   lc_temp VARCHAR2(15) := clientid;

BEGIN

       BEGIN
    SELECT cm_clnt_name, cm_ead_flag, cm_cust_clnt_id
      INTO clnt_name, eadcheck, lc_custclntid
      FROM sys_clnt_mstr
     WHERE cm_clnt_id = clientid OR cm_cust_clnt_id = clientpqid;
            -- SELECT cm_clnt_name,cm_ead_flag,CM_CUST_CLNT_ID into clnt_name,eadcheck,lc_custClntId from sys_clnt_mstr where cm_clnt_id=clientId;
          EXCEPTION
         WHEN OTHERS THEN
            clnt_name:=NULL;
       END;


    BEGIN

    IF lc_temp is NULL THEN

    SELECT am_addr_code, am_pe_site_id, am_phno1, am_strt_name, am_drnr,
           am_gety_levl, am_gety_type, am_gety_code, am_mail_id
     INTO  am_addr_code, site_id, phno1, strt_name, drnr,
           lc_levl, lc_type, lc_code, lc_email
    FROM sys_clnt_mstr, sys_addr_mstr
    WHERE cm_cust_clnt_id = clientpqid
    and CM_CLNT_ID = am_enty_id
    and am_defa_flag = 'Y' AND am_addr_type = 'CLNT';

    SELECT CM_CLNT_ID into lc_clientId from sys_clnt_mstr where cm_cust_clnt_id = clientpqid;

    else
     SELECT am_addr_code, am_pe_site_id, am_phno1, am_strt_name, am_drnr,
     am_gety_levl, am_gety_type, am_gety_code, am_mail_id
    INTO am_addr_code, site_id, phno1, strt_name, drnr,
    lc_levl, lc_type, lc_code, lc_email
    FROM sys_addr_mstr
    WHERE am_enty_id = clientid AND am_defa_flag = 'Y' AND am_addr_type = 'CLNT';
     end if;


       EXCEPTION
         WHEN OTHERS THEN
            brnc_id:=NULL;
    end;

    BEGIN
    pack_web.PRO_ASSIGN_DEST_BRNC(lc_code,lc_type,lc_levl,brnc_id,BRNC_NAME,site_id);

      --   select BM_BRNC_NAME into BRNC_NAME from sys_brnc_mstr where BM_BRNC_ID=brnc_id;
       EXCEPTION
       WHEN OTHERS THEN
        BRNC_NAME:=NULL;
    end;


        pro_get_city(brnc_id,city,POSTALTYPE,POSTALLEVEL,POSTALCODE,addrefno);

      BEGIN
       SELECT ZIP_CODE INTO POSTAL
           FROM SYS_GETY_MSTR
           WHERE    GE_LEVL=lc_levl AND
             GE_TYPE=lc_type AND
               GE_CODE = lc_code;
      EXCEPTION
     WHEN OTHERS THEN
        POSTAL:=NULL;
      END;


    SELECT GE_DESC into dis_col  FROM SYS_GETY_MSTR
    WHERE ge_levl = upper(lc_levl)
    AND ge_type = UPPER(lc_type)
    AND ge_code = upper(lc_code);

   SELECT gr_levl_r,gr_type_r,gr_code_r,ge_desc
   INTO ln_lr,ln_tr,lc_mcd,lc_gdesc
   FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
   WHERE GR_CODE = UPPER(lc_code)
   AND gr_levl =lc_levl
   AND gr_type =lc_type
   AND GM.GE_LEVL=GR.GR_LEVL_R AND GM.GE_TYPE=GR.GR_TYPE_R
   AND GM.GE_CODE=GR.GR_CODE_R;
    --Display Colonia
     IF ln_lr= 6 THEN -- AND ln_tr = 1
         dis_col:= lc_gdesc ;
     END IF;
    --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
        de_po_pais:= lc_gdesc ;
     END IF;
  LOOP
     SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC
     INTO ln_lr,ln_tr,lc_mcd,lc_gdesc FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
     WHERE GR_LEVL=ln_lr AND GR_TYPE=ln_tr
      AND GR_CODE=lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R
      AND GM.GE_CODE=GR.GR_CODE_R;

   --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
         de_po_pais:= lc_gdesc ;
     END IF;
   --Display City
    IF ln_lr= 4 THEN --AND ln_tr = 1
       dis_city := lc_gdesc;
    END IF;
   -- Display Muncipio
    IF ln_lr =3 THEN --AND ln_tr =1
        di_muncipo := lc_gdesc;
    end if;
   -- Display State
    IF ln_lr = 2 THEN -- AND ln_tr = 1
         dis_state := lc_gdesc;
    end if;
   -- Display Country
    IF ln_lr = 1 THEN --AND ln_tr = 1
         dis_con := lc_gdesc;
    end if;
    IF LN_LR = 1 THEN --AND LN_TR = 1
    EXIT;
    END IF;
   END LOOP;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
       lc_gdesc := null;
    WHEN OTHERS THEN
       lc_gdesc := null;
END;*/
/*PROCEDURE PRO_POST_ADDRQ(clientId VARCHAR2,POSTAL out varchar2,dis_col out varchar2,de_po_pais out varchar2,dis_city out varchar2,di_muncipo out varchar2,dis_state out varchar2,dis_con out varchar2,clnt_name out varchar2,brnc_id out varchar2,phno1 out varchar2,strt_name out varchar2,drnr out varchar2,BRNC_NAME out varchar2,eadcheck out varchar2, lc_levl out VARCHAR2,
   lc_type out VARCHAR2,lc_code out VARCHAR2, POSTALLEVEL OUT VARCHAR2,POSTALTYPE OUT VARCHAR2,POSTALCODE OUT VARCHAR2) IS


   ln_lr  VARCHAR2(15);
   ln_tr  VARCHAR2(15);
   lc_mcd VARCHAR2(15);
   lc_gdesc VARCHAR2(60);
   lc_gdesc4 VARCHAR2(500);
   lc_gdesc3 VARCHAR2(500);
   lc_gdesc2 VARCHAR2(500);
   MSGNUM NUMBER;
   ln_dummy NUMBER;
   city varchar2(100);
   addrefno varchar2(100);
   site_id varchar2(100);
   AM_ADDR_CODE varchar2(100);

BEGIN
       BEGIN

            SELECT cm_clnt_name,cm_ead_flag into clnt_name,eadcheck from sys_clnt_mstr where cm_clnt_id=clientId;
          EXCEPTION
         WHEN OTHERS THEN
            clnt_name:=NULL;
       END;

    BEGIN
              select AM_ADDR_CODE,am_pe_site_id,am_phno1,am_strt_name,am_drnr,am_gety_levl, am_gety_type, am_gety_code into
        AM_ADDR_CODE,site_id,phno1,strt_name,drnr,lc_levl,lc_type,lc_code from sys_addr_mstr where am_enty_id=clientId and am_defa_flag = 'Y' and am_addr_type ='CLNT';
       EXCEPTION
         WHEN OTHERS THEN
        brnc_id:=NULL;
    end;



    BEGIN
    pack_web.PRO_ASSIGN_DEST_BRNC(lc_code,lc_type,lc_levl,brnc_id,BRNC_NAME,site_id);

      --   select BM_BRNC_NAME into BRNC_NAME from sys_brnc_mstr where BM_BRNC_ID=brnc_id;
       EXCEPTION
       WHEN OTHERS THEN
        BRNC_NAME:=NULL;
    end;


        pro_get_city(brnc_id,city,POSTALTYPE,POSTALLEVEL,POSTALCODE,addrefno);

      BEGIN
       SELECT ZIP_CODE INTO POSTAL
           FROM SYS_GETY_MSTR
           WHERE    GE_LEVL=lc_levl AND
             GE_TYPE=lc_type AND
               GE_CODE = lc_code;
      EXCEPTION
     WHEN OTHERS THEN
        POSTAL:=NULL;
      END;


    SELECT GE_DESC into dis_col  FROM SYS_GETY_MSTR
    WHERE ge_levl = upper(lc_levl)
    AND ge_type = UPPER(lc_type)
    AND ge_code = upper(lc_code);

   SELECT gr_levl_r,gr_type_r,gr_code_r,ge_desc
   INTO ln_lr,ln_tr,lc_mcd,lc_gdesc
   FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
   WHERE GR_CODE = UPPER(lc_code)
   AND gr_levl =lc_levl
   AND gr_type =lc_type
   AND GM.GE_LEVL=GR.GR_LEVL_R AND GM.GE_TYPE=GR.GR_TYPE_R
   AND GM.GE_CODE=GR.GR_CODE_R;
    --Display Colonia
     IF ln_lr= 6 THEN -- AND ln_tr = 1
         dis_col:= lc_gdesc ;
     END IF;
    --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
        de_po_pais:= lc_gdesc ;
     END IF;
  LOOP
     SELECT GR_LEVL_R,GR_TYPE_R,GR_CODE_R,GE_DESC
     INTO ln_lr,ln_tr,lc_mcd,lc_gdesc FROM SYS_GETY_RESP GR,SYS_GETY_MSTR GM
     WHERE GR_LEVL=ln_lr AND GR_TYPE=ln_tr
      AND GR_CODE=lc_mcd AND GM.GE_LEVL=GR.GR_LEVL_R AND
      GM.GE_TYPE=GR.GR_TYPE_R
      AND GM.GE_CODE=GR.GR_CODE_R;

   --Display Delegacion\Poblacion\Pais-----
     IF ln_lr= 5 THEN
         de_po_pais:= lc_gdesc ;
     END IF;
   --Display City
    IF ln_lr= 4 THEN --AND ln_tr = 1
       dis_city := lc_gdesc;
    END IF;
   -- Display Muncipio
    IF ln_lr =3 THEN --AND ln_tr =1
        di_muncipo := lc_gdesc;
    end if;
   -- Display State
    IF ln_lr = 2 THEN -- AND ln_tr = 1
         dis_state := lc_gdesc;
    end if;
   -- Display Country
    IF ln_lr = 1 THEN --AND ln_tr = 1
         dis_con := lc_gdesc;
    end if;
    IF LN_LR = 1 THEN --AND LN_TR = 1
    EXIT;
    END IF;
   END LOOP;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
       lc_gdesc := null;
    WHEN OTHERS THEN
       lc_gdesc := null;
END;

-----------
*/
-----------------------------------------------------
FUNCTION Fun_ftch_site_name(Site_id IN VARCHAR2) RETURN VARCHAR2 IS
        lv_site_name SYS_SITE_MSTR.SM_SITE_NAME%TYPE;
        CURSOR CUR_GET_SITE_NAME(LV_SITE_ID VARCHAR2) IS
            SELECT SM_SITE_NAME
             FROM SYS_SITE_MSTR
             WHERE SM_SITE_ID=LV_SITE_ID;
    BEGIN
        OPEN CUR_GET_SITE_NAME(Site_id);
        FETCH CUR_GET_SITE_NAME INTO LV_SITE_NAME;
        IF CUR_GET_SITE_NAME%NOTFOUND THEN
            LV_SITE_NAME := NULL;
        END IF;
        CLOSE CUR_GET_SITE_NAME;
        RETURN(lv_site_name);
    END Fun_ftch_site_name;
----------------------------------------------------------
PROCEDURE PRO_ASSIGN_ORIGIN_BRNC (GH_DEST_CLNT_ID VARCHAR2, OUT_DEST_BR OUT VARCHAR2 )IS
 /*CURSOR CUR_GETY_DTLS IS
              SELECT AM_GETY_CODE,
                                         AM_GETY_TYPE,
                     AM_GETY_LEVL
              FROM   SYS_ADDR_MSTR
              WHERE  AM_ENTY_ID = GH_DEST_CLNT_ID
              AND    AM_ADDR_TYPE = 'CLNT'
              AND AM_DEFA_FLAG ='Y';
             -- AND    AM_ADDR_STYP = 'FISCAL';

 cursor getSite is
                 select WC_ASSN_SITE from web_clnt_mstr where WC_CLNT_ID = GH_DEST_CLNT_ID;
 cursor cur_get_branch(LV_SITE1 varchar2)  is
                 select BM_BRNC_ID from sys_brnc_mstr where BM_BRNC_SITE_ID=LV_SITE1  AND BM_FLAG1='G';


 CURSOR CUR_DEST_BR (ARG_GE_CODE IN VARCHAR2, ARG_GE_TYPE IN VARCHAR2, ARG_GE_LEVL IN VARCHAR2) IS
                             SELECT BC_BRNC_ID
                             FROM ER_BRNC_COLY_MSTR
                             WHERE BC_GETY_CODE_6=ARG_GE_CODE
                             AND BC_GETY_TYPE_6=ARG_GE_TYPE
                             AND BC_GETY_LEVL_6=ARG_GE_LEVL;
   LV_GE_CODE SYS_ADDR_MSTR.AM_GETY_CODE%TYPE;
   LV_GE_TYPE SYS_ADDR_MSTR.AM_GETY_TYPE%TYPE;
   LV_GE_LEVEL    SYS_ADDR_MSTR.AM_GETY_LEVL%TYPE;
   LV_SITE VARCHAR2(50);
   ln_clnt number;
   lv_index number;

BEGIN
     FOR REC_AM IN CUR_GETY_DTLS
     LOOP
         LV_GE_CODE := REC_AM.AM_GETY_CODE;
         LV_GE_TYPE := REC_AM.AM_GETY_TYPE;
         LV_GE_LEVEL := REC_AM.AM_GETY_LEVL;
     END LOOP;



FOR REC IN CUR_DEST_BR (LV_GE_CODE,LV_GE_TYPE,    LV_GE_LEVEL)
   LOOP
         OUT_DEST_BR := REC.BC_BRNC_ID;
   END LOOP;
begin
open getSite;
fetch getSite into LV_SITE;
close getSite;
end;
begin
    SELECT COUNT(*) into ln_clnt
      FROM   SYS_BRNC_MSTR
      WHERE  BM_BRNC_SITE_ID= LV_SITE
      AND    BM_FLAG1='G';
 Exception
   when others then
   ln_clnt:=1;
end;

begin
if ln_clnt=1 then
open cur_get_branch(LV_SITE);
fetch cur_get_branch into OUT_DEST_BR;
close cur_get_branch;
end if;
EXCEPTION
   WHEN OTHERS THEN OUT_DEST_BR := NULL;
end;
*/
cursor getSite is
                 select WC_ASSN_SITE from web_clnt_mstr where WC_CLNT_ID = GH_DEST_CLNT_ID;

cursor cur_get_branch(LV_SITE1 varchar2)  is
                 select BM_BRNC_ID from sys_brnc_mstr where BM_BRNC_SITE_ID=LV_SITE1  AND BM_FLAG1='G';


CURSOR CUR_GET_BRNC IS
              SELECT WC_APRV_BRNC
              FROM   WEB_CLNT_MSTR WHERE WC_CLNT_ID=GH_DEST_CLNT_ID;

ln_clnt number;
   lv_index number;
   LV_SITE VARCHAR2(50);
begin

    open getSite;
fetch getSite into LV_SITE;
close getSite;

    SELECT COUNT(*) into ln_clnt
      FROM   SYS_BRNC_MSTR
      WHERE  BM_BRNC_SITE_ID= LV_SITE
      AND    BM_FLAG1='G';
 --Exception
   --when others then
   --ln_clnt:=1;

if ln_clnt=1 then
open cur_get_branch(LV_SITE);
fetch cur_get_branch into OUT_DEST_BR;
close cur_get_branch;
end if;

        IF ln_clnt>1 then
              OPEN CUR_GET_BRNC;
              FETCH CUR_GET_BRNC INTO OUT_DEST_BR;
              CLOSE CUR_GET_BRNC;
        END IF;




             -- EXCEPTION
                 --WHEN OTHERS THEN OUT_DEST_BR := NULL;
--              END;


END PRO_ASSIGN_ORIGIN_BRNC;
-------------------------------------------------------------------------
--PROCEDURE PRO_ASSIGN_DEST_BRNC (LV_GE_CODE VARCHAR2,LV_GE_TYPE VARCHAR2, LV_GE_LEVEL VARCHAR2,OUT_DEST_BR OUT VARCHAR2,OUT_DEST_BR_NAME OUT VARCHAR2,DEST_SITE VARCHAR2)IS
--
--
-- --CURSOR CUR_GETY_DTLS IS
--            --  SELECT AM_GETY_CODE,
--                --                         AM_GETY_TYPE,
--            --         AM_GETY_LEVL
--           --   FROM   SYS_ADDR_MSTR
--           --   WHERE  AM_ENTY_ID = GH_DEST_CLNT_ID
--          --    AND    AM_ADDR_CODE = ADDRESS_CODE
--            --;
--             -- AND    AM_ADDR_STYP = 'FISCAL';
--
--   CURSOR C1(LV_CODE1 VARCHAR2,LV_LEVL1 VARCHAR2,LV_TYP1 VARCHAR2) IS
--         SELECT GR_CODE_R, GR_LEVL_R, GR_TYPE_R
--         FROM SYS_GETY_RESP
--         WHERE (GR_CODE,GR_LEVL,GR_TYPE) IN (
--             SELECT GR_CODE_R, GR_LEVL_R, GR_TYPE_R
--                 FROM SYS_GETY_RESP
--                 WHERE GR_CODE=LV_CODE1 AND GR_LEVL=LV_LEVL1 AND GR_TYPE=LV_TYP1);
--  LV_GE_CODE_R SYS_GETY_RESP.GR_CODE_R%TYPE;
--  LV_GE_LEVL_R SYS_GETY_RESP.GR_LEVL_R%TYPE;
--  LV_GE_TYPE_R SYS_GETY_RESP.GR_TYPE_R%TYPE;
--
--
--  CURSOR C3(LV_CODE3 VARCHAR2,LV_LEVL3 VARCHAR2,LV_TYP3 VARCHAR2) IS
--  SELECT GE_DESC,GE_CODE
--  FROM SYS_GETY_MSTR
--  WHERE GE_CODE=LV_CODE3 and GE_LEVL=LV_LEVL3 AND GE_TYPE=LV_TYP3;
--
--  CURSOR CUR_DEST_BR(ARG_GE_CODE IN VARCHAR2, ARG_GE_CODE1 IN VARCHAR2) IS
--                             SELECT BC_BRNC_ID
--                             FROM ER_BRNC_COLY_MSTR
--                             WHERE BC_GETY_CODE_6=ARG_GE_CODE
--                            -- AND BC_GETY_TYPE_6=ARG_GE_TYPE
--                            -- AND BC_GETY_LEVL_6=ARG_GE_LEVL;
--                             AND BC_GETY_CODE_4=ARG_GE_CODE1;
--
--   LV_GE_CODE1 SYS_GETY_MSTR.GE_CODE%TYPE;
--   LV_GE_DESC SYS_GETY_MSTR.GE_DESC%TYPE;
--
--   LV_COLONY SYS_GETY_MSTR.GE_DESC%TYPE;
--   LV_COLONY_CODE SYS_GETY_MSTR.GE_CODE%TYPE;
--
--CURSOR CUR_DEST_BRNC_NAME(BR_CODE VARCHAR2) IS
--select pack_web.fun_ftch_brnc_name(BR_CODE) from dual;
--BEGIN
--     --FOR REC_AM IN CUR_GETY_DTLS
--     --LOOP
--         --OPEN CUR_GETY_DTLS;
--        --FETCH CUR_GETY_DTLS INTO LV_GE_CODE,LV_GE_TYPE,LV_GE_LEVEL;
----        LV_GE_CODE := REC_AM.AM_GETY_CODE;
--    --     LV_GE_TYPE := REC_AM.AM_GETY_TYPE;
--         --LV_GE_LEVEL := REC_AM.AM_GETY_LEVL;
--    -- --END LOOP;
--    -- CLOSE CUR_GETY_DTLS;
-- BEGIN
--    OPEN C1(LV_GE_CODE,LV_GE_LEVEL,LV_GE_TYPE);
--    FETCH C1 INTO LV_GE_CODE_R,LV_GE_LEVL_R,LV_GE_TYPE_R;
--    CLOSE C1;
--  END;
--  --dbms_output.put_line('LV_GE_CODE_R'||LV_GE_CODE_R);
--  --dbms_output.put_line('LV_GE_LEVL_R'||LV_GE_LEVL_R);
--  --dbms_output.put_line('LV_GE_TYPE_R'||LV_GE_TYPE_R);
--
--  BEGIN
--    OPEN C3(LV_GE_CODE_R,LV_GE_LEVL_R,LV_GE_TYPE_R);
--    FETCH C3 INTO  LV_GE_DESC,LV_GE_CODE1;
--        IF C3%NOTFOUND THEN
--          LV_GE_DESC := NULL;
--      END IF;
--    CLOSE C3;
--  END;
----
--  BEGIN
--      OPEN C3(LV_GE_CODE,LV_GE_LEVEL,LV_GE_TYPE);
--      FETCH C3 INTO LV_COLONY,LV_COLONY_CODE ;
--        IF C3%NOTFOUND THEN
--          LV_COLONY := NULL;
--      END IF;
--      CLOSE C3;
--  END;
--    ---
--    --dbms_output.put_line('LV_GE_CODE'||LV_GE_CODE);
--     --dbms_output.put_line('LV_GE_CODE1'||LV_GE_CODE1);
----    OPEN CUR_DEST_BR(LV_GE_CODE,LV_GE_CODE1);
--  BEGIN
--     OPEN CUR_DEST_BR(LV_COLONY_CODE,LV_GE_CODE1);
--        LOOP
--         FETCH CUR_DEST_BR INTO OUT_DEST_BR;
--         EXIT WHEN CUR_DEST_BR%NOTFOUND;
--
--         IF(DEST_SITE=substr(OUT_DEST_BR,1,3)) then
--         EXIT;
--        END IF;
--
--         END LOOP;
--       --FOR REC IN C1 (LV_GE_CODE,LV_GE_TYPE,LV_GE_LEVEL)
--       --LOOP
--    --         OUT_DEST_BR := REC.BC_BRNC_ID;
--    CLOSE CUR_DEST_BR;
--  END;
--
--      OPEN CUR_DEST_BRNC_NAME(OUT_DEST_BR);
--      FETCH CUR_DEST_BRNC_NAME INTO OUT_DEST_BR_NAME;
--      CLOSE CUR_DEST_BRNC_NAME;
--   --END LOOP;
--EXCEPTION
--   WHEN OTHERS THEN OUT_DEST_BR := NULL;
--END PRO_ASSIGN_DEST_BRNC;

PROCEDURE PRO_ASSIGN_DEST_BRNC (LV_GE_CODE VARCHAR2,LV_GE_TYPE VARCHAR2, LV_GE_LEVEL VARCHAR2,OUT_DEST_BR OUT VARCHAR2,OUT_DEST_BR_NAME OUT VARCHAR2,DEST_SITE VARCHAR2)IS

     CURSOR CUR_DEST_BR(ARG_GE_CODE VARCHAR, ARG_GE_LEVEL VARCHAR, ARG_GE_TYPE VARCHAR2) IS
        SELECT SUCURSAL, PACK_WEB.Fun_ftch_brnc_name(SUCURSAL) 
         FROM PCOBERTURA_VIEW 
         WHERE COD_COLO = ARG_GE_CODE AND COL_LEVL = ARG_GE_LEVEL AND COL_TYPE = ARG_GE_TYPE;

  BEGIN
    OPEN CUR_DEST_BR(LV_GE_CODE, LV_GE_LEVEL, LV_GE_TYPE);
    LOOP
        FETCH CUR_DEST_BR INTO OUT_DEST_BR, OUT_DEST_BR_NAME;
        EXIT WHEN CUR_DEST_BR%NOTFOUND;
    END LOOP;
    CLOSE CUR_DEST_BR;       
  EXCEPTION WHEN OTHERS THEN 
    OUT_DEST_BR := NULL;
  
END PRO_ASSIGN_DEST_BRNC;
-------------------------------------
FUNCTION fun_chk_no_of_br(GH_DEST_SITE_ID varchar2) RETURN NUMBER  IS
  CURSOR CUR_NO_OF_BR IS
      SELECT COUNT(*)
      FROM   SYS_BRNC_MSTR
      WHERE  BM_BRNC_SITE_ID=GH_DEST_SITE_ID
      AND    BM_FLAG1='G';
      LN_CNT NUMBER :=0;
BEGIN
   OPEN CUR_NO_OF_BR;
   FETCH CUR_NO_OF_BR INTO LN_CNT;
   CLOSE CUR_NO_OF_BR;
   RETURN LN_CNT;
EXCEPTION
    WHEN OTHERS THEN RETURN 1;
END fun_chk_no_of_br;
--------------------------------
FUNCTION fun_chk_phy_dc_exist(GH_DEST_SITE_ID varchar2) RETURN NUMBER IS
  CURSOR CUR_PHY_DC IS
     SELECT COUNT(*)
     FROM   SYS_BRNC_MSTR
     WHERE  BM_FLAG1='D'
     AND    NVL(BM_BRNC_DC_VRTUL,'N')='N'
     AND    BM_BRNC_SITE_ID=GH_DEST_SITE_ID;

     LN_CNT NUMBER;
BEGIN
  OPEN CUR_PHY_DC;
  FETCH CUR_PHY_DC INTO LN_CNT;
  CLOSE CUR_PHY_DC;
  RETURN LN_CNT;
EXCEPTION
    WHEN OTHERS THEN RETURN 0;
END fun_chk_phy_dc_exist;
-----------------------------
procedure pro_insrt_web_ctrl_email(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2, CE_DEST_CLNT_ID VARCHAR2,
     CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2,
     out_file1 OUT VARCHAR2) IS

BEGIN
    INSERT INTO WEB_CTRL_EMAIL (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)
                        VALUES (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_DEST, CE_EMAIL_ORGN, CE_STUS_ENVIO, SYSDATE, CRTD_BY, SYSDATE, CRTD_BY);

EXCEPTION
    WHEN OTHERS THEN out_file1 := 'Error '||SQLCODE||': '||SQLERRM;
END pro_insrt_web_ctrl_email;
procedure pro_insrt_web_ctrl_email_right(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2, CE_DEST_CLNT_ID VARCHAR2,
     CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2,
     out_file1 OUT VARCHAR2) IS

BEGIN
    INSERT INTO WEB_CTRL_EMAIL (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)
                        VALUES (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, SYSDATE, CRTD_BY, SYSDATE, CRTD_BY);

EXCEPTION
    WHEN OTHERS THEN out_file1 := 'Error '||SQLCODE||': '||SQLERRM;
END pro_insrt_web_ctrl_email_right;
procedure pro_insrt_web_ctrl_email_tmp(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2, CE_DEST_CLNT_ID VARCHAR2,
     CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2,
     out_file1 OUT VARCHAR2) IS

BEGIN
    INSERT INTO WEB_CTRL_EMAIL_TMP (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)
                        VALUES (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_DEST, CE_EMAIL_ORGN, CE_STUS_ENVIO, SYSDATE, CRTD_BY, SYSDATE, CRTD_BY);

EXCEPTION
    WHEN OTHERS THEN out_file1 := 'Error '||SQLCODE||': '||SQLERRM;
END pro_insrt_web_ctrl_email_tmp;
----------------------------------------
procedure pro_insrt_web_ctrl_email_tmpr(CE_GUIA_NO VARCHAR2, CE_ORGN_CLNT_ID VARCHAR2, CE_DEST_CLNT_ID VARCHAR2,
     CE_EMAIL_ORGN VARCHAR2, CE_EMAIL_DEST VARCHAR2, CE_STUS_ENVIO VARCHAR2, CRTD_BY VARCHAR2,
     out_file1 OUT VARCHAR2) IS

BEGIN
    INSERT INTO WEB_CTRL_EMAIL_TMP (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)
                        VALUES (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, SYSDATE, CRTD_BY, SYSDATE, CRTD_BY);

EXCEPTION
    WHEN OTHERS THEN out_file1 := 'Error '||SQLCODE||': '||SQLERRM;
END pro_insrt_web_ctrl_email_tmpr;
PROCEDURE pro_val_input_email(CLNT_ORGN_ID VARCHAR2, CLNT_DEST_ID VARCHAR2, EMAIL_ORGN OUT VARCHAR2, EMAIL_DEST OUT VARCHAR2) IS
   LV_CLNT_ID VARCHAR2 (1);
   LV_EMAIL_ORGN VARCHAR2(1);
   LV_EMAIL_DEST VARCHAR2(1);

   CURSOR CUR_GET_EMAIL_ORG(LV_CLNT VARCHAR2) IS
   SELECT SN_NOTIFY_ORGN
   FROM SYS_NOTIFY_MSTR
   WHERE SN_CLNT_ID = LV_CLNT AND (SN_NOTIFY_ORGN IS NOT NULL AND SN_NOTIFY_ORGN = 'Y');

   CURSOR CUR_GET_EMAIL_ORG_DEST(LV_CLNT VARCHAR2) IS
   SELECT SN_NOTIFY_DEST
   FROM SYS_NOTIFY_MSTR
   WHERE SN_CLNT_ID = LV_CLNT AND (SN_NOTIFY_DEST IS NOT NULL AND SN_NOTIFY_DEST = 'Y');

   CURSOR CUR_GET_EMAIL_DEST(LV_CLNT VARCHAR2) IS
   SELECT SN_NOTIFY_REC
   FROM SYS_NOTIFY_MSTR
   WHERE SN_CLNT_ID = LV_CLNT AND (SN_NOTIFY_REC IS NOT NULL AND SN_NOTIFY_REC = 'Y');

BEGIN
   OPEN CUR_GET_EMAIL_ORG(CLNT_ORGN_ID);
   FETCH CUR_GET_EMAIL_ORG INTO LV_EMAIL_ORGN;
   IF CUR_GET_EMAIL_ORG%NOTFOUND THEN
      LV_EMAIL_ORGN := 'N';
   END IF;
   CLOSE CUR_GET_EMAIL_ORG;

   OPEN CUR_GET_EMAIL_ORG_DEST(CLNT_ORGN_ID);
   FETCH CUR_GET_EMAIL_ORG_DEST INTO LV_EMAIL_DEST;
   IF CUR_GET_EMAIL_ORG_DEST%NOTFOUND THEN
      LV_EMAIL_DEST := 'N';
   END IF;
   CLOSE CUR_GET_EMAIL_ORG_DEST;

   IF LV_EMAIL_DEST = 'N' THEN
    BEGIN
       OPEN CUR_GET_EMAIL_DEST(CLNT_DEST_ID);
       FETCH CUR_GET_EMAIL_DEST INTO LV_EMAIL_DEST;
       IF CUR_GET_EMAIL_DEST%NOTFOUND THEN
          LV_EMAIL_DEST := 'N';
       END IF;
       CLOSE CUR_GET_EMAIL_DEST;
    END;
   END IF;
   EMAIL_ORGN := LV_EMAIL_ORGN;
   EMAIL_DEST := LV_EMAIL_DEST;
EXCEPTION
    WHEN OTHERS THEN
       EMAIL_ORGN := 'N';
       EMAIL_DEST := 'N';
END pro_val_input_email;
PROCEDURE PRO_CARGA_CLNT_DEST(ORGN_BRNC_ID VARCHAR2, ORGN_CLNT_ID VARCHAR2, CLNT_FUENTE VARCHAR2, FECHA_INI VARCHAR2, FECHA_FIN VARCHAR2, USUARIO_SIPWEB VARCHAR2) IS

M_DEST_CLNT_ID VARCHAR2(15);
M_DEST_SITE_ID VARCHAR2(10);
M_EXISTE NUMBER;

CURSOR CLIENTES_DESTINO IS
SELECT DISTINCT GH_DEST_CLNT_ID, GH_DEST_SITE_ID FROM BOK_GUIA_HEAD
WHERE
GH_ORGN_BRNC_ID=ORGN_BRNC_ID AND
GH_ISSE_DATE >= to_date(FECHA_INI, 'DD/MM/YYYY') AND
GH_ISSE_DATE <= to_date(FECHA_FIN, 'DD/MM/YYYY')+23.9998/24 AND
GH_GUIA_TYPE='N' AND
GH_ORGN_CLNT_ID=CLNT_FUENTE;

BEGIN
   OPEN CLIENTES_DESTINO;
   LOOP
      FETCH CLIENTES_DESTINO INTO M_DEST_CLNT_ID, M_DEST_SITE_ID;
      IF CLIENTES_DESTINO%NOTFOUND THEN
         EXIT;
      END IF;

      -- VERIFICO SI YA EXISTE EL CLIENTE DESTINO --
      M_EXISTE:=0;
      BEGIN
         SELECT COUNT(*) INTO M_EXISTE
         FROM WEB_ORGN_DEST_CLNT
         WHERE WO_ORGN_CLNT_ID = ORGN_CLNT_ID AND WO_DEST_CLNT_ID=M_DEST_CLNT_ID;
         EXCEPTION
         WHEN NO_DATA_FOUND THEN
            M_EXISTE:=0;
      END;

      IF M_EXISTE=0 THEN
         -- INSERTO EN WEB_ORGN_DEST_CLNT --
         INSERT INTO WEB_ORGN_DEST_CLNT
         (WO_ORGN_CLNT_ID, WO_DEST_CLNT_ID, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, WO_DEST_SITE_ID, WO_DEST_CUST_CLNT_ID)
         VALUES (
         ORGN_CLNT_ID, M_DEST_CLNT_ID, SYSDATE, USUARIO_SIPWEB, SYSDATE, USUARIO_SIPWEB, M_DEST_SITE_ID, NULL);
         COMMIT;
      END IF;

   END LOOP;
   CLOSE CLIENTES_DESTINO;

END PRO_CARGA_CLNT_DEST;
------------------
PROCEDURE pro_ftch_guia_for_generate_RR (orgn_clnt_id VARCHAR2, orgn_brnc_id IN OUT VARCHAR2, form_no IN OUT VARCHAR2,
    guia_no IN OUT VARCHAR2, brnc_name OUT VARCHAR2, dest_clnt_name OUT VARCHAR2, isse_date OUT VARCHAR2, guia_amnt OUT NUMBER,
    mesg_type OUT VARCHAR2, mesg_text OUT VARCHAR2, site_name OUT VARCHAR2, docu_type OUT VARCHAR2, mesg_id OUT NUMBER,
    orgn_site_name OUT VARCHAR2, orgn_brnc_name OUT VARCHAR2, orgn_clnt_name OUT VARCHAR2, guia_no_return  OUT VARCHAR2, dest_brnc_id OUT VARCHAR2, radFlag OUT VARCHAR2)
IS
   lc_docu_type VARCHAR2(1):='Q';
   lc_guia_type VARCHAR2(1):='H';
   lc_guia_type_Normal varchar2(1):='N';
   lc_acty_flag varchar2(1):='A';
   G_OR_F VARCHAR2(1);
   site_id BOK_GUIA_HEAD.GH_DEST_SITE_ID%TYPE;
   site_id_org BOK_GUIA_HEAD.GH_ORG_SITE_ID%TYPE;
   lc_actv_flag BOK_GUIA_HEAD.gh_actv_flag%TYPE;
   lc_dest_brnc_id     BOK_GUIA_HEAD.GH_DEST_BRNC_ID%TYPE;
   lc_orgn_brnc_id     BOK_GUIA_HEAD.GH_ORGN_BRNC_ID%TYPE;
   lc_orgn_clnt_id     BOK_GUIA_HEAD.GH_ORGN_CLNT_ID%TYPE;
   lc_guia_in_mnft   BOK_GUIA_HEAD.GH_GUIA_NO%TYPE;
   lc_guia_refr_no    BOK_GUIA_HEAD.GH_GUIA_REFR_NO%TYPE;    -- added by Hari on 25-03-2003
   --lc_docu_type   BOK_GUIA_HEAD.GH_DOCU_TYPE%TYPE;
   lc_cliente_convenio VARCHAR2(15);
   lc_clnt_miembro VARCHAR(15);
   lc_clnt_miembro_site VARCHAR(5);
   lc_guia_N_activa number(1);
   lv_gh_curr_loca BOK_GUIA_HEAD.GH_CURR_LOCA%TYPE;
   lv_gh_curr_dest BOK_GUIA_HEAD.GH_CURR_DEST%TYPE;
   
   CURSOR cur_ftch_form_no IS
   SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
   GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
   GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID, GH_DOCU_TYPE, GH_ORGN_CLNT_NAME, GH_ORG_SITE_ID, GH_RAD_FLAG
   FROM BOK_GUIA_HEAD
   WHERE GH_FORM_NO= FORM_NO    AND
   GH_PREP_BRNC_ID = orgn_brnc_id AND
   GH_DOCU_TYPE = lc_docu_type;

   CURSOR cur_ftch_guia_no IS
   SELECT GH_ORGN_BRNC_ID, GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_ORGN_CLNT_ID,
   GH_DEST_CLNT_NAME, TO_CHAR(GH_ISSE_DATE,'dd/mm/yyyy hh24:mi'), GH_GUIA_AMNT,
   GH_ACTV_FLAG, GH_GUIA_REFR_NO,GH_DEST_SITE_ID, GH_DOCU_TYPE, GH_ORGN_CLNT_NAME, GH_ORG_SITE_ID, GH_RAD_FLAG
   FROM BOK_GUIA_HEAD
   WHERE GH_GUIA_NO= GUIA_NO
   AND GH_GUIA_TYPE = lc_guia_type;

   CURSOR cur_list_of_members IS
    SELECT WC_MBR_ID, WC_SITE_ID
    FROM WEB_CLNT_MBR
    WHERE WC_CLNT_ID = lc_cliente_convenio;

BEGIN
   --IF orgn_brnc_id IS NOT NULL AND form_no IS NOT NULL THEN
   --100% pass the origin branch id for both, ie for form no and for guia
   IF form_no IS NOT NULL THEN
      G_OR_F:='F';
      OPEN cur_ftch_form_no;
      FETCH cur_ftch_form_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
      lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id, docu_type, orgn_clnt_name, site_id_org, radFlag;
      CLOSE cur_ftch_form_no;
   ELSE
      G_OR_F:='G';
      OPEN cur_ftch_guia_no;
       FETCH cur_ftch_guia_no INTO lc_orgn_brnc_id, form_no, guia_no, lc_dest_brnc_id,
       lc_orgn_clnt_id, dest_clnt_name, isse_date, guia_amnt, lc_actv_flag, lc_guia_refr_no,site_id, docu_type, orgn_clnt_name, site_id_org, radFlag;
       CLOSE cur_ftch_guia_no;
   END IF;

   IF lc_actv_flag ='I' THEN --This will either have 'A' - Active or 'I' - Converted to Guia
        lc_guia_N_activa := 0;
        SELECT COUNT(GH_GUIA_NO) INTO lc_guia_N_activa
        FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = guia_no AND GH_GUIA_TYPE = lc_guia_type_Normal AND GH_ACTV_FLAG = lc_acty_flag;
        
        SELECT gh_curr_loca, gh_curr_dest INTO lv_gh_curr_loca ,   lv_gh_curr_dest
        FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = guia_no AND GH_GUIA_TYPE = lc_guia_type_Normal AND GH_ACTV_FLAG = lc_acty_flag;
        IF lc_guia_N_activa = 0 THEN
              mesg_id := 9900002; -- Significa que tanto la guia H o N (si hubiera) estan inactivas
              PRO_SHOW_MESG('WEB','ESP',mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
        ELSIF SUBSTR(form_no,0,2) <> 'WW' THEN
              --SOLO GUIAS DE DOCUMENTACION EN LINEA PUEDEN GENERAR GUIAS DE RETORNO (GUIAS SERIE WW)
              mesg_id := 800100;
              PRO_SHOW_MESG('WEB','ESP',mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
        ELSIF lc_orgn_clnt_id<>orgn_clnt_id THEN
              SELECT NVL(WC_CLNT_ID,'') INTO lc_cliente_convenio FROM WEB_CLNT_MBR WHERE WC_MBR_ID =  orgn_clnt_id;
              OPEN cur_list_of_members;
              LOOP
                    FETCH cur_list_of_members INTO lc_clnt_miembro, lc_clnt_miembro_site;
                    IF lc_clnt_miembro = lc_orgn_clnt_id THEN
                          IF lc_clnt_miembro_site <> SUBSTR(orgn_brnc_id,0,3) THEN
                                --Aqui va el mensaje: La guia debe ser generada en tal plaza: lc_clnt_miembro_site
                                mesg_id := 800101;
                                PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
                                mesg_text:= mesg_text || ' ' || fun_ftch_site_name(lc_clnt_miembro_site);
                                EXIT;--Salimos del ciclo cuando encontremos
                          END IF;
                    ELSE
                          --Entro aqu¡ por que la guia no pertenece a otro cliente convenio
                          mesg_id := 800003;
                          PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
                    END IF;
                    EXIT WHEN cur_list_of_members%notfound;
              END LOOP;
              CLOSE cur_list_of_members;
        ELSIF SUBSTR(lc_orgn_brnc_id,0,3)<>SUBSTR(orgn_brnc_id,0,3) THEN
              mesg_id := 800004;
              PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
        ELSIF lv_gh_curr_loca != lv_gh_curr_dest THEN
              mesg_id := 800102;--LA GUIA NO HA SIDO RECIBIDA EN DESTINO AUN
              PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
        ELSE 
              select COUNT(*)  INTO lc_guia_N_activa from WEB_GUIA_REFR where GR_GUIA_REFR = guia_no;
              
              IF lc_guia_N_activa > 0 THEN
                   select gr_guia_no INTO guia_no_return from WEB_GUIA_REFR where GR_GUIA_REFR = guia_no and crtd_on = (select max(crtd_on)  from WEB_GUIA_REFR where GR_GUIA_REFR = guia_no) ;
              END IF; 
              IF lc_dest_brnc_id IS NOT NULL THEN --Record found
                   brnc_name:= Pack_Web.fun_ftch_brnc_name(lc_dest_brnc_id);
                   dest_brnc_id := lc_dest_brnc_id;
              END IF;
              IF site_id IS NOT NULL THEN --Record found
                   site_name:= Pack_Web.fun_ftch_site_name(site_id);
              END IF;
              IF lc_orgn_brnc_id IS NOT NULL THEN --Record found
                   orgn_brnc_name:= Pack_Web.fun_ftch_brnc_name(lc_orgn_brnc_id);
              END IF;
              IF site_id_org IS NOT NULL THEN --Record found
                   orgn_site_name:= Pack_Web.fun_ftch_site_name(site_id_org);
              END IF;
        END IF;
   ELSIF lc_actv_flag ='A' THEN
        mesg_id := 800102;
        PRO_SHOW_MESG('WEB',language_id,mesg_id,1,NULL,NULL,NULL,mesg_type,mesg_text);
   ELSE
        IF G_OR_F='F' THEN
              mesg_id := 900130;
              PRO_SHOW_MESG('BOK',language_id,mesg_id,1,form_no,NULL,NULL,mesg_type,mesg_text);
        ELSE
              mesg_id := 900140;
              PRO_SHOW_MESG('BOK',language_id,mesg_id,1,guia_no,NULL,NULL,mesg_type,mesg_text);
        END IF;
   END IF;
END;
------------------
FUNCTION fun_ftch_trif_amnt_km_SEG(clnt_id VARCHAR2, km_dist NUMBER, serv_id VARCHAR2, qty NUMBER, srvc_itmid VARCHAR2, WEIGHT number, 
VLUM number, PESO_REAL NUMBER, PESO_VOLUMETRICO NUMBER,  tariff_id OUT varchar2,  clnt_dis out number , VEAD OUT NUMBER, VRAD OUT NUMBER)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
        SELECT  WCK_TRIF_AMNT,WCK_TRIF_AMNT_EXED, WCK_FACTOR_VALUE
        FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM
        WHERE WCK_ORGN_CLNT_ID = clnt_id
        AND km_dist between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM
        AND WCK_SRVC_ID = serv_id
        AND WCK_TRIF_AMNT <> 0 ORDER BY WCK_FACTOR_VALUE;
    tarifbase SEG_TARIF_MSTR.TRIF_AMNT%TYPE;
    tarifExce SEG_TARIF_MSTR.TRIF_AMNT_EXED%TYPE;
    ln_factor NUMBER;
    ln_cant NUMBER;
    ln_imp_tar number;
    factr_type VARCHAR2(15):='KG';
    peso_exce number;
    peso_vol number; 
    peso_considerar number;
    lv_tarifbase SEG_TARIF_MSTR.TRIF_AMNT%TYPE;
    lv_factor NUMBER;
BEGIN
    lv_tarifbase :=0;
    IF PESO_REAL  > PESO_VOLUMETRICO THEN
      peso_considerar := PESO_REAL;
    ELSE
      peso_considerar := PESO_VOLUMETRICO;
    END IF;    
    OPEN cur_ftch_trif_amnt;
     LOOP
        FETCH cur_ftch_trif_amnt INTO tarifbase, tarifExce, ln_factor;
           IF cur_ftch_trif_amnt%NOTFOUND THEN
               --DBMS_OUTPUT.PUT_LINE ( '---------peso_considerar '||peso_considerar|| ' ln_factor '||ln_factor);
              peso_exce :=peso_considerar-LN_FACTOR;
              --DBMS_OUTPUT.PUT_LINE ( '---------XXXpeso_exce '||peso_exce);
              EXIT;
           END IF;
           
           IF peso_considerar > LN_FACTOR THEN
              lv_tarifbase := lv_tarifbase+tarifbase;
           ELSE
              peso_exce :=0;
              lv_tarifbase := lv_tarifbase+tarifbase;
              EXIT ;
           END IF;
           --DBMS_OUTPUT.PUT_LINE ('tarifbase '||tarifbase|| ' tarifExce '||tarifExce||' ln_factor '||ln_factor);
           --DBMS_OUTPUT.PUT_LINE ('peso_considerar '||peso_considerar||' lv_tarifbase '||lv_tarifbase||' peso_exce '||peso_exce);
      END LOOP;
    CLOSE cur_ftch_trif_amnt;
    IF srvc_itmid='PACKETS' THEN
        tariff_id :=  PACK_GLOBAL_TARIFF.FUN_GET_TARIFF (WEIGHT ,VLUM ) ; /* obtener la tarifa*/
     ELSE
     tariff_id:='S';
     FACTR_TYPE:='NON';
    END IF;

    ln_imp_tar := qty * (lv_tarifbase+(peso_exce*tarifExce ));
    
     VEAD:=  ln_imp_tar * (FUN_GET_SRVC_PORC('EAD')/100);
     VRAD:=  ln_imp_tar * (FUN_GET_SRVC_PORC('RAD')/100);
     ln_imp_tar := ln_imp_tar * (FUN_GET_SRVC_PORC(srvc_itmid)/100);
    RETURN(ln_imp_tar);
END;
------------------
FUNCTION fun_ftch_trif_amnt_brnc_SEG(clnt_id VARCHAR2,dest_brnc VARCHAR2, serv_id VARCHAR2, orgn_brnc VARCHAR2, qty NUMBER, srvc_itmid VARCHAR2, WEIGHT number, 
VLUM number, PESO_REAL NUMBER, PESO_VOLUMETRICO NUMBER,  tariff_id OUT varchar2,  clnt_dis out number , VEAD OUT NUMBER, VRAD OUT NUMBER)
    RETURN NUMBER IS
    CURSOR cur_ftch_trif_amnt IS
    SELECT WCP_TRIF_AMNT, WCP_TRIF_AMNT_EXED, WCP_FACTOR_VALUE
        FROM WEB_CLNT_SRVC_TRIF_FACTOR
        WHERE WCP_ORGN_CLNT_ID = clnt_id
        AND substr(WCP_ORGN_BRNC_ID,1,3) = substr(orgn_brnc,1,3)
        AND substr(WCP_DEST_BRNC_ID,1,3) = substr(dest_brnc,1,3)
        AND WCP_SRVC_ID = serv_id
        AND WCP_TRIF_AMNT <> 0 order by WCP_FACTOR_VALUE;
    tarifbase SEG_TARIF_MSTR.TRIF_AMNT%TYPE;
    tarifExce SEG_TARIF_MSTR.TRIF_AMNT_EXED%TYPE;
    ln_factor NUMBER;
    ln_cant NUMBER;
    ln_imp_tar number;
    factr_type VARCHAR2(15):='KG';
    peso_exce number;
    peso_vol number; 
    peso_considerar number;
    lv_tarifbase SEG_TARIF_MSTR.TRIF_AMNT%TYPE;
    lv_factor NUMBER;
BEGIN
    lv_tarifbase :=0;
    IF PESO_REAL  > PESO_VOLUMETRICO THEN
      peso_considerar := PESO_REAL;
    ELSE
      peso_considerar := PESO_VOLUMETRICO;
    END IF; 
    OPEN cur_ftch_trif_amnt;
     LOOP
        FETCH cur_ftch_trif_amnt INTO tarifbase, tarifExce, ln_factor;
           IF cur_ftch_trif_amnt%NOTFOUND THEN
               --DBMS_OUTPUT.PUT_LINE ( '---------peso_considerar '||peso_considerar|| ' ln_factor '||ln_factor);
              peso_exce :=peso_considerar-LN_FACTOR;
              --DBMS_OUTPUT.PUT_LINE ( '---------XXXpeso_exce '||peso_exce);
              EXIT;
           END IF;
           
           IF peso_considerar > LN_FACTOR THEN
              lv_tarifbase := lv_tarifbase+tarifbase;
           ELSE
              peso_exce :=0;
              lv_tarifbase := lv_tarifbase+tarifbase;
              EXIT ;
           END IF;
           --DBMS_OUTPUT.PUT_LINE ('tarifbase '||tarifbase|| ' tarifExce '||tarifExce||' ln_factor '||ln_factor);
           --DBMS_OUTPUT.PUT_LINE ('peso_considerar '||peso_considerar||' lv_tarifbase '||lv_tarifbase||' peso_exce '||peso_exce);
      END LOOP;
    CLOSE cur_ftch_trif_amnt;
    IF srvc_itmid='PACKETS' THEN
        tariff_id :=  PACK_GLOBAL_TARIFF.FUN_GET_TARIFF (WEIGHT ,VLUM ) ; /* obtener la tarifa*/
     ELSE
     tariff_id:='S';
     FACTR_TYPE:='NON';
    END IF;
    
    ln_imp_tar := qty * (lv_tarifbase+(peso_exce*tarifExce ));
    
     VEAD:=  ln_imp_tar * (FUN_GET_SRVC_PORC('EAD')/100);
     VRAD:=  ln_imp_tar * (FUN_GET_SRVC_PORC('RAD')/100);
     ln_imp_tar := ln_imp_tar * (FUN_GET_SRVC_PORC(srvc_itmid)/100);
    RETURN(ln_imp_tar);
END;

------------------
PROCEDURE PRO_INSRT_WEB_BOK_SRVC_CIA(GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,DISC IN NUMBER,
                                 TAX IN NUMBER,TAX_RET IN NUMBER,AMOUNT IN NUMBER ,
                                 FLAG IN VARCHAR2,SRVC_TYPE IN VARCHAR2,
                                 DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2,
                                 CRETD_ON IN DATE,CRETD_BY IN VARCHAR2,
                                 MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                 STUS_FLAG IN VARCHAR2,DISC_SLAB IN VARCHAR2, CMPY_ID IN VARCHAR2)IS
BEGIN
     INSERT INTO BOK_GUIA_SRVC(GS_GUIA_NO,
                               GS_SRVC_ID,
                               GS_DISC,
                               GS_TAX,
                               GS_TAX_RET,
                               GS_SUB_TOTL,
                               GS_ADD_PYMT_FLAG,
                               GS_SRVC_TYPE,
                               GS_DOCU_TYPE,
                               GS_GUIA_TYPE,
                               CRTD_ON,
                               CRTD_BY,
                               MDFD_ON,
                               MDFD_BY,
                               GS_STUS_FLAG,
                               GS_DISC_SLAB_NO,
                               GS_CMPY_ID)
                        VALUES
                               (GUIA_NO,
                                SRVC,
                                ROUND(NVL(DISC,0),2),
                                ROUND(NVL(TAX,0),2),
                                ROUND(NVL(TAX_RET,0),2),
                                ROUND(NVL(AMOUNT,0),2),
                                FLAG,
                                SRVC_TYPE,
                                DOCU_TYPE,
                                GUIA_TYPE,
                                CRETD_ON,
                                CRETD_BY,
                                MODI_ON,
                                MODI_BY,
                                STUS_FLAG,
                                DISC_SLAB,
                                CMPY_ID);
END;
------------------
PROCEDURE PRO_INSRT_WEBBOKSRVCITEM_CIA (GUIA_NO IN VARCHAR2,SRVC IN VARCHAR2,REFR_SRVC IN VARCHAR2,
                                       DESCR IN VARCHAR2,CONTENT IN VARCHAR2,
                                       QTY IN VARCHAR2,SRVC_CHRG IN NUMBER,
                                       STUS_FLAG IN VARCHAR2,
                                       CREATD_ON IN DATE,CREATD_BY IN VARCHAR2,
                                       MODI_ON IN DATE,MODI_BY IN VARCHAR2,
                                       SLAB IN VARCHAR2,
                                       WEIGHT IN NUMBER,VOLUME IN NUMBER,
                                       DOCU_TYPE IN VARCHAR2,GUIA_TYPE IN VARCHAR2, CMPY_ID IN VARCHAR2) IS
BEGIN
  INSERT INTO BOK_GUIA_SRVC_ITEM(GL_GUIA_NO,GL_SRVC_ID,GL_REFR_SRVC_ID,GL_DESC,
                                 GL_CONT,GL_QUNT,GL_SRVC_CHRG,
                                 GL_STUS_FLAG,
                                 CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY,
                                 GL_SLAB_NO,
                                 GL_VLUE_1,GL_VLUE_2,
                                 GL_DOCU_TYPE,GL_GUIA_TYPE, GL_CMPY_ID)
                          VALUES
                                (GUIA_NO,SRVC,REFR_SRVC,
                                 DESCR,CONTENT,QTY,
                                 ROUND(NVL(SRVC_CHRG,0),2),
                                 STUS_FLAG,
                                 CREATD_ON,CREATD_BY,MODI_ON,MODI_BY,
                                 SLAB,
                                 ROUND(NVL(WEIGHT,0),2),
                                 ROUND(NVL(VOLUME,0),2),
                                 DOCU_TYPE,GUIA_TYPE, CMPY_ID);

END;
END;
/