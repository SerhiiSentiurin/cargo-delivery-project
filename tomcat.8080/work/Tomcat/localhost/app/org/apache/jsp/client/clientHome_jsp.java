/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/10.0.14
 * Generated at: 2022-09-29 14:41:04 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.client;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;

public final class clientHome_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final jakarta.servlet.jsp.JspFactory _jspxFactory =
          jakarta.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(4);
    _jspx_dependants.put("file:/C:/Users/Серёга/.m2/repository/org/glassfish/web/jakarta.servlet.jsp.jstl/2.0.0/jakarta.servlet.jsp.jstl-2.0.0.jar", Long.valueOf(1652529507264L));
    _jspx_dependants.put("/view/header.jsp", Long.valueOf(1664360011846L));
    _jspx_dependants.put("jar:file:/C:/Users/Серёга/.m2/repository/org/glassfish/web/jakarta.servlet.jsp.jstl/2.0.0/jakarta.servlet.jsp.jstl-2.0.0.jar!/META-INF/c.tld", Long.valueOf(1602863172000L));
    _jspx_dependants.put("/all/getCost.jsp", Long.valueOf(1664462463598L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("jakarta.servlet");
    _jspx_imports_packages.add("jakarta.servlet.http");
    _jspx_imports_packages.add("jakarta.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;

  private volatile jakarta.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public jakarta.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
  }

  public void _jspService(final jakarta.servlet.http.HttpServletRequest request, final jakarta.servlet.http.HttpServletResponse response)
      throws java.io.IOException, jakarta.servlet.ServletException {

    if (!jakarta.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final jakarta.servlet.jsp.PageContext pageContext;
    jakarta.servlet.http.HttpSession session = null;
    final jakarta.servlet.ServletContext application;
    final jakarta.servlet.ServletConfig config;
    jakarta.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    jakarta.servlet.jsp.JspWriter _jspx_out = null;
    jakarta.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\r\n");
      out.write("    <body>\r\n");
      out.write("        <head>\r\n");
      out.write("            <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT\" crossorigin=\"anonymous\">\r\n");
      out.write("            ");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta charset=\"UTF-8\">\r\n");
      out.write("        <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT\" crossorigin=\"anonymous\">\r\n");
      out.write("        <div class=\"card\">\r\n");
      out.write("            <div class=\"card-header\" >\r\n");
      out.write("                <div class=\"d-grid gap-2 d-md-flex justify-content-md-end\">\r\n");
      out.write("                    <div class=\"card-body\">\r\n");
      out.write("                        <h5 class=\"card-title\"> Cargo Application </h5>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <form action = \"/app/cargo/logout\" method = \"POST\" >\r\n");
      out.write("                        <input type = \"submit\" class=\"btn btn-primary\" value = 'Logout'>\r\n");
      out.write("                    </form>\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </head>\r\n");
      out.write("</html>");
      out.write("\r\n");
      out.write("        </head>\r\n");
      out.write("        <h2>\r\n");
      out.write("            This is Client home page! user id: ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.id}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\r\n");
      out.write("        </h2>\r\n");
      out.write("\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <body>\r\n");
      out.write("        <head>\r\n");
      out.write("            <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT\" crossorigin=\"anonymous\">\r\n");
      out.write("            <script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc\"></script>\r\n");
      out.write("        </head>\r\n");
      out.write("        <div align = \"left\">\r\n");
      out.write("            <button class = \"btn btn-primary\" onclick=\"location.href='/app/index.jsp'\">Back to registration</button>\r\n");
      out.write("            <h2 align = \"center\">\r\n");
      out.write("                Calculate the delivery cost!\r\n");
      out.write("            </h2>\r\n");
      out.write("        </div>\r\n");
      out.write("\r\n");
      out.write("        <div class=\"input-group mb-3\">\r\n");
      out.write("            <label class=\"input-group-text\" for=\"senderCity\">Choose departure city</label>\r\n");
      out.write("            <select class=\"form-select\"  name = \"senderCity\" form = \"calculateCost\" required>\r\n");
      out.write("                <option value=\"\" selected>Choose...</option>\r\n");
      out.write("                ");
      if (_jspx_meth_c_005fforEach_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("            </select>\r\n");
      out.write("        </div>\r\n");
      out.write("\r\n");
      out.write("        <div class=\"input-group mb-3\">\r\n");
      out.write("            <label class=\"input-group-text\" for=\"recipientCity\">Choose arrival city</label>\r\n");
      out.write("            <select class=\"form-select\"  name = \"recipientCity\" form = \"calculateCost\" required>\r\n");
      out.write("                <option value=\"\" selected>Choose...</option>\r\n");
      out.write("                ");
      if (_jspx_meth_c_005fforEach_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("            </select>\r\n");
      out.write("        </div>\r\n");
      out.write("\r\n");
      out.write("        <table class=\"table table-striped\">\r\n");
      out.write("            <td align = \"center\">\r\n");
      out.write("                <form action = \"/app/cargo/calculateDelivery\" method = \"GET\" id = \"calculateCost\">\r\n");
      out.write("                    <label for = \"Weight\" >Weight (kg)</label>\r\n");
      out.write("                    <input type = \"number\" min = \"0\" max= \"5000\" step = 0.01 name=\"weight\" id = \"Weight\" required><br><br>\r\n");
      out.write("                    <label for = \"Volume\">Volume (m³)</label>\r\n");
      out.write("                    <input type = \"number\" min = \"0\" max= \"25\" step = 0.01 name=\"volume\" id = \"Volume\" required><br><br>\r\n");
      out.write("                    <input type = \"hidden\" name = \"senderCity\" value = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${senderCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\">\r\n");
      out.write("                    <input type = \"hidden\" name = \"recipientCity\" value = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${recipientCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\">\r\n");
      out.write("                    <button type = \"submit\"  class = \"btn btn-secondary\">Get cost</button>\r\n");
      out.write("                </form>\r\n");
      out.write("\r\n");
      out.write("            </td>\r\n");
      out.write("            <td>\r\n");
      out.write("                ");
      if (_jspx_meth_c_005fif_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("            </td>\r\n");
      out.write("            <td align = \"center\">\r\n");
      out.write("                <div id=\"mapa\">\r\n");
      out.write("                </div>\r\n");
      out.write("            </td>\r\n");
      out.write("            <td>\r\n");
      out.write("                <h5>\r\n");
      out.write("                    Some rules how to use this application...\r\n");
      out.write("                </h5>\r\n");
      out.write("            </td>\r\n");
      out.write("        </table>\r\n");
      out.write("        <br>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<style>\r\n");
      out.write("#mapa {\r\n");
      out.write("	height: 550px;\r\n");
      out.write("	width: 800px;\r\n");
      out.write("	max-width: 800px;\r\n");
      out.write("	display:block;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("    </body>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("var calcCoordX = 50.4019514; var calcCoordY = 30.3926095;\r\n");
      out.write("\r\n");
      out.write("var directionsDisplayy;\r\n");
      out.write("var directionsServicee = new google.maps.DirectionsService();\r\n");
      out.write("var geocoder = new google.maps.Geocoder();\r\n");
      out.write("var map;\r\n");
      out.write("\r\n");
      out.write("function initialize() {\r\n");
      out.write("\r\n");
      out.write("directionsDisplayy = new google.maps.DirectionsRenderer();\r\n");
      out.write("var chicagot = new google.maps.LatLng(calcCoordX, calcCoordY);\r\n");
      out.write("  var myOptions = {\r\n");
      out.write("    zoom:10,\r\n");
      out.write("    mapTypeId: google.maps.MapTypeId.ROADMAP,\r\n");
      out.write("    center: chicagot\r\n");
      out.write("  }\r\n");
      out.write("  map = new google.maps.Map(document.getElementById(\"mapa\"), myOptions);\r\n");
      out.write("  directionsDisplayy.setMap(map);\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function calcRoutee() {\r\n");
      out.write("  var from = document.getElementById(\"from\").value;\r\n");
      out.write("  var to = document.getElementById(\"to\").value;\r\n");
      out.write("  var request = {\r\n");
      out.write("    origin:from,\r\n");
      out.write("    destination:to,\r\n");
      out.write("    travelMode: google.maps.TravelMode.DRIVING\r\n");
      out.write("  };\r\n");
      out.write("  directionsServicee.route(request, function(response, status) {\r\n");
      out.write("    if (status == google.maps.DirectionsStatus.OK) {\r\n");
      out.write("      directionsDisplayy.setDirections(response);\r\n");
      out.write("      var route = response.routes[0];\r\n");
      out.write("    }\r\n");
      out.write("  });\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function addHandler(ev, handler){\r\n");
      out.write("	try{\r\n");
      out.write("		window.addEventListener(ev, handler, false);\r\n");
      out.write("	}catch(e){\r\n");
      out.write("		window.attachEvent('on'+ev, handler);\r\n");
      out.write("	}\r\n");
      out.write("}\r\n");
      out.write("addHandler('load', initialize);\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("</html>");
      out.write("\r\n");
      out.write("\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof jakarta.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_005fforEach_005f0(jakarta.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    jakarta.servlet.jsp.PageContext pageContext = _jspx_page_context;
    jakarta.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f0 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    boolean _jspx_th_c_005fforEach_005f0_reused = false;
    try {
      _jspx_th_c_005fforEach_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fforEach_005f0.setParent(null);
      // /all/getCost.jsp(20,16) name = items type = jakarta.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
      _jspx_th_c_005fforEach_005f0.setItems(new org.apache.jasper.el.JspValueExpression("/all/getCost.jsp(20,16) '${routeSender}'",_jsp_getExpressionFactory().createValueExpression(_jspx_page_context.getELContext(),"${routeSender}",java.lang.Object.class)).getValue(_jspx_page_context.getELContext()));
      // /all/getCost.jsp(20,16) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fforEach_005f0.setVar("route");
      int[] _jspx_push_body_count_c_005fforEach_005f0 = new int[] { 0 };
      try {
        int _jspx_eval_c_005fforEach_005f0 = _jspx_th_c_005fforEach_005f0.doStartTag();
        if (_jspx_eval_c_005fforEach_005f0 != jakarta.servlet.jsp.tagext.Tag.SKIP_BODY) {
          do {
            out.write("\r\n");
            out.write("                <option value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${route.senderCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
            out.write('"');
            out.write('>');
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${route.senderCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
            out.write("</option>\r\n");
            out.write("                ");
            int evalDoAfterBody = _jspx_th_c_005fforEach_005f0.doAfterBody();
            if (evalDoAfterBody != jakarta.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
        }
        if (_jspx_th_c_005fforEach_005f0.doEndTag() == jakarta.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          return true;
        }
      } catch (java.lang.Throwable _jspx_exception) {
        while (_jspx_push_body_count_c_005fforEach_005f0[0]-- > 0)
          out = _jspx_page_context.popBody();
        _jspx_th_c_005fforEach_005f0.doCatch(_jspx_exception);
      } finally {
        _jspx_th_c_005fforEach_005f0.doFinally();
      }
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f0);
      _jspx_th_c_005fforEach_005f0_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_c_005fforEach_005f0, _jsp_getInstanceManager(), _jspx_th_c_005fforEach_005f0_reused);
    }
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f1(jakarta.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    jakarta.servlet.jsp.PageContext pageContext = _jspx_page_context;
    jakarta.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f1 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    boolean _jspx_th_c_005fforEach_005f1_reused = false;
    try {
      _jspx_th_c_005fforEach_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fforEach_005f1.setParent(null);
      // /all/getCost.jsp(30,16) name = items type = jakarta.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
      _jspx_th_c_005fforEach_005f1.setItems(new org.apache.jasper.el.JspValueExpression("/all/getCost.jsp(30,16) '${routeRecipient}'",_jsp_getExpressionFactory().createValueExpression(_jspx_page_context.getELContext(),"${routeRecipient}",java.lang.Object.class)).getValue(_jspx_page_context.getELContext()));
      // /all/getCost.jsp(30,16) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fforEach_005f1.setVar("route");
      int[] _jspx_push_body_count_c_005fforEach_005f1 = new int[] { 0 };
      try {
        int _jspx_eval_c_005fforEach_005f1 = _jspx_th_c_005fforEach_005f1.doStartTag();
        if (_jspx_eval_c_005fforEach_005f1 != jakarta.servlet.jsp.tagext.Tag.SKIP_BODY) {
          do {
            out.write("\r\n");
            out.write("                <option value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${route.recipientCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
            out.write('"');
            out.write('>');
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${route.recipientCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
            out.write("</option>\r\n");
            out.write("                ");
            int evalDoAfterBody = _jspx_th_c_005fforEach_005f1.doAfterBody();
            if (evalDoAfterBody != jakarta.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
        }
        if (_jspx_th_c_005fforEach_005f1.doEndTag() == jakarta.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          return true;
        }
      } catch (java.lang.Throwable _jspx_exception) {
        while (_jspx_push_body_count_c_005fforEach_005f1[0]-- > 0)
          out = _jspx_page_context.popBody();
        _jspx_th_c_005fforEach_005f1.doCatch(_jspx_exception);
      } finally {
        _jspx_th_c_005fforEach_005f1.doFinally();
      }
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f1);
      _jspx_th_c_005fforEach_005f1_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_c_005fforEach_005f1, _jsp_getInstanceManager(), _jspx_th_c_005fforEach_005f1_reused);
    }
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f0(jakarta.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    jakarta.servlet.jsp.PageContext pageContext = _jspx_page_context;
    jakarta.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    boolean _jspx_th_c_005fif_005f0_reused = false;
    try {
      _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fif_005f0.setParent(null);
      // /all/getCost.jsp(50,16) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order != null}", boolean.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null)).booleanValue());
      int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
      if (_jspx_eval_c_005fif_005f0 != jakarta.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("                    <div id = \"results\">\r\n");
          out.write("                        <h3>Results: </h4>\r\n");
          out.write("                        <h5>Sender city: ");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.senderCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("</h5>\r\n");
          out.write("                        <h5>Arrival city: ");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.recipientCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("</h5>\r\n");
          out.write("                        <h5>Distance: ");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.distance}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("</h5>\r\n");
          out.write("                        <h5>Delivery cost: ");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.deliveryCost}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("</h5>\r\n");
          out.write("\r\n");
          out.write("                        <input type = \"hidden\" id = \"from\" value = \"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.senderCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("\">\r\n");
          out.write("                        <input type = \"hidden\" id = \"to\" value = \"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${order.recipientCity}", java.lang.String.class, (jakarta.servlet.jsp.PageContext)_jspx_page_context, null));
          out.write("\">\r\n");
          out.write("                        <button type = \"submit\" onclick=\"calcRoutee()\" class = \"btn btn-secondary\">Get route</button>\r\n");
          out.write("                    </div>\r\n");
          out.write("                ");
          int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
          if (evalDoAfterBody != jakarta.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fif_005f0.doEndTag() == jakarta.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      _jspx_th_c_005fif_005f0_reused = true;
    } finally {
      org.apache.jasper.runtime.JspRuntimeLibrary.releaseTag(_jspx_th_c_005fif_005f0, _jsp_getInstanceManager(), _jspx_th_c_005fif_005f0_reused);
    }
    return false;
  }
}
