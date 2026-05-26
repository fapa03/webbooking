package bean;

import logger.AccessLog;
import beanUtil.ConnectDB;

import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse;
import com.paquetexpress.www.webbooking.GuiasRetorno.Data;
import com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales;
import com.paquetexpress.www.webbooking.GuiasRetorno.GuiasRetornoProxy;
import com.paquetexpress.www.webbooking.GuiasRetorno.Header;
import com.paquetexpress.www.webbooking.GuiasRetorno.SolicitudEnvio;
import com.paquetexpress.www.webbooking.GuiasRetorno.TypeEvent;

public class GuiasRetorno {
	private StringBuffer cnct = new StringBuffer();
	//private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	public DataResponse getGuiaRetorno(String rastreo, String guia, int cantidadRastreos, String tipoRecoleccion, String horaIni, String horaFinal, String userId) {
		
		Data data = null;
		DataResponse dataResponse = null;
		SolicitudEnvio solicitudEnvio = new SolicitudEnvio();
		try {
			solicitudEnvio.setRastreoNo(rastreo);
			solicitudEnvio.setGuiaNo(guia);
			solicitudEnvio.setCantidadRastreos(cantidadRastreos);
			com.paquetexpress.www.webbooking.GuiasRetorno.DatoAdicional adicional= new com.paquetexpress.www.webbooking.GuiasRetorno.DatoAdicional();
			adicional.setClaveDataAd("tipoRecoleccion");
			adicional.setValorDataAd(tipoRecoleccion == null? "1" : tipoRecoleccion);
			adicional.setDataAditional1(horaIni == null? "" : horaIni);
			adicional.setDataAditional2(horaFinal == null ? "" : horaFinal);
			adicional.setDataAditional3("");
			adicional.setDataAditional4("");
			com.paquetexpress.www.webbooking.GuiasRetorno.DatoAdicional[] datoAdi= new com.paquetexpress.www.webbooking.GuiasRetorno.DatoAdicional[1];
			datoAdi[0]=adicional;
			com.paquetexpress.www.webbooking.GuiasRetorno.DatosAdicionales datosAdicionales= new DatosAdicionales();
			datosAdicionales.setDatoAdicional(datoAdi);
			solicitudEnvio.setDatosAdicionales(datosAdicionales);
			data = new Data(
					new Header(userId, "", "", TypeEvent.Confirmar),
					solicitudEnvio
					);
			GuiasRetornoProxy guiasRetornoProxy = new GuiasRetornoProxy(ConnectDB.getEndPointGuiaRetorno());
			dataResponse = guiasRetornoProxy.genGuiasRetorno(data);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getGuiaRetorno()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return dataResponse;
	}
}
