package mx.com.paquetexpress.facade;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mx.com.paquetexpress.dao.SiteChangeDAO;
import mx.com.paquetexpress.dto.SiteChangeDTO;
import org.codehaus.jackson.map.ObjectMapper;

import bean.ConsultaParametros;
import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class SiteChange
 */
public class SiteChange extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SiteChangeDAO dao = new SiteChangeDAO();
	Resources resources = new Resources();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SiteChange() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1 
		response.setHeader("Pragma","no-cache"); //HTTP 1.0 
		response.setDateHeader ("Expires", 0); 
		// TODO Auto-generated method stub
		ArrayList<SiteChangeDTO>lista = null;
		HttpSession session = request.getSession();
		String usuario = "";
		Global global = (Global) session.getAttribute("sGlobal");
		usuario = global.getOrigenUserClave();
		lista = dao.getPlazasAsignadas(usuario);
		Gson gson = new Gson();
		JsonElement element = gson.toJsonTree(lista, new TypeToken<ArrayList<SiteChangeDTO>>(){}.getType());
		JsonArray jsonArray = element.getAsJsonArray();
		response.setContentType("application/json");
		response.getWriter().print(jsonArray);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapeador = new ObjectMapper(); 
		SiteChangeDTO dto = mapeador.readValue(request.getInputStream(), SiteChangeDTO.class);
		HttpSession session = request.getSession();
		session.setAttribute("sAssignedBranch", dto.getSucursal());
        session.setAttribute("branchid", dto.getSucursal());//AAP VARIABLE PARA GUIAS DE PREPAGO
        session.setAttribute("branchName", dto.getSucursalNombre());//AAP VARIABLE PARA GUIAS DE PREPAGO
        session.setAttribute("sSiteId", dto.getSite());
        session.setAttribute("siteid", dto.getSite());//AAP VARIABLE PARA GUIAS DE PREPAGO
        session.setAttribute("sClientId", dto.getClienteMiembro());
        
        Global global = (Global)session.getAttribute("sGlobal");

        global.setClientId(dto.getClienteMiembro());
        global.setClientName(dto.getMiembroNombre());
        global.setAssignedBranch(dto.getSucursal());
        global.setAssignedSite(dto.getSite());
        global.setSiteName(dto.getSiteName());
        
        try (Connection con = ConnectDB.getConnection()){
        	getGroupClientId(con, global);
            global.setAllowCancelGuiaMult(getCancelGuiaMult(con, global.getClientId()));
            global.setShowGuiasRR(getShowElaboracionGuiasRR(con, global.getClientId()));
        }catch (Exception e) {
			e.printStackTrace();
		}
       
        session.setAttribute("sGlobal", global);
		
		response.setContentType("application/json");
		response.getWriter().println("{\"ok\":true}");
		
	}

	public void getGroupClientId(Connection con, Global global) {

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid,to_char(sysdate,'DD/MM/YYYY HH24:MI') from dual";
			pst = con.prepareStatement(groupIdQuery);
			pst.setString(1, global.getClientId());
			rs = pst.executeQuery();
			while (rs.next()) {
				global.setGroupClientId(rs.getString(1));
				global.setSysDate(rs.getString(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}

	public String getCancelGuiaMult(Connection con, String clntId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, clntId, "ALLOWCNCLGMULT");
			if (temp) {
				result = "Y";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getShowElaboracionGuiasRR(Connection con, String strClientOrigId) {
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "SHW_GGUIA_RR", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp.get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
