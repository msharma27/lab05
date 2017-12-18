package controllers;

import java.io.IOException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import DAO.DBConnector;
import DAO.stockDAO;
@ManagedBean
@SessionScoped
public class sellController {

    private static final long serialVersionUID = 1L;
    static final String API_KEY = "AF93E6L5I01EA39O";
    public static Connection connector;
    private String symbol;
    private double price;
    private int qty;
    private double amt;
    private String table1Markup;
    private String table2Markup;

    private String selectedSymbol;
    private List<SelectItem> availableSymbols;
 
    public List<String> list; 
    

    public String getSellSymbol() {
        if (getRequestParameter("symbol") != null) {
            symbol = getRequestParameter("symbol");
        }
        return symbol;
    }
    
    public void setSellSymbol(String sellSymbol) {
        System.out.println("func setSellSymbol()");  //check
    }
   
    public String getPurchaseSymbol() {
        if (getRequestParameter("symbol") != null) {
            symbol = getRequestParameter("symbol");
        }
        return symbol;
    }
    
    public void setPurchaseSymbol(String purchaseSymbol) {
        System.out.println("func setPurchaseSymbol()");  //check
    }

    public double getPurchasePrice() {
        if (getRequestParameter("price") != null) {
            price = Double.parseDouble(getRequestParameter("price"));
            System.out.println("price: " + price);
        }
        return price;
    }

    public void setPurchasePrice(double purchasePrice) {
        System.out.println("func setPurchasePrice()");  //check
    }
    
    public double getSellPrice() {
        if (getRequestParameter("price") != null) {
            price = Double.parseDouble(getRequestParameter("price"));
            System.out.println("price: " + price);
        }
        return price;
    }

    public void setSellPrice(double sellPrice) {
        System.out.println("func setSellPrice()");  //check
    }
    
    private String getRequestParameter(String name) {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(name);
    }

    @PostConstruct
    public void init() {
        //initially populate stock list
        List<String> symbols = getPurchasedStockSymbols();
        availableSymbols = new ArrayList<SelectItem>();   
        for(String symbol:symbols) {
        	availableSymbols.add(new SelectItem(symbol,symbol));
        }
        //initially populate intervals for stock api
        availableIntervals = new ArrayList<SelectItem>();
        availableIntervals.add(new SelectItem("1min", "1min"));
        availableIntervals.add(new SelectItem("5min", "5min"));
        availableIntervals.add(new SelectItem("15min", "15min"));
        availableIntervals.add(new SelectItem("30min", "30min"));
        availableIntervals.add(new SelectItem("60min", "60min"));
    }

    private String selectedInterval;
    private List<SelectItem> availableIntervals;

    public String getSelectedInterval() {
        return selectedInterval;
    }

    public void setSelectedInterval(String selectedInterval) {
        this.selectedInterval = selectedInterval;
    }

    public List<SelectItem> getAvailableIntervals() {
        return availableIntervals;
    }

    public void setAvailableIntervals(List<SelectItem> availableIntervals) {
        this.availableIntervals = availableIntervals;
    }

    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<SelectItem> getAvailableSymbols() {
        return availableSymbols;
    }

    public void setAvailableSymbols(List<SelectItem> availableSymbols) {
        this.availableSymbols = availableSymbols;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getTable1Markup() {
        return table1Markup;
    }

    public void setTable1Markup(String table1Markup) {
        this.table1Markup = table1Markup;
    }

    public String getTable2Markup() {
        return table2Markup;
    }

    public void setTable2Markup(String table2Markup) {
        this.table2Markup = table2Markup;
    }
    
   
    //for purchase stocks
    public String createDbRecord(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");
        	connector = DBConnector.createConnector();
            Statement statement = connector.createStatement();
            
            //get userid
            String uname = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("sessionUname").toString();
            
            System.out.println(uname);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            statement.executeUpdate("INSERT INTO `purchase` (`id`, `stock_symbol`, `qty`, `price`, `amt`,`uname`) "
                    + "VALUES (NULL,'" + symbol + "','" + qty + "','" + price + "','" + amt +"', '" + uname + "');");
            
            statement.close();
            connector.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "purchaseResult";
    }
    
    public List<String> getPurchasedStockSymbols(){
    	List<String> list = new ArrayList<String>();
    	 try {
         	connector = DBConnector.createConnector();            
             String uname = FacesContext.getCurrentInstance()
                     .getExternalContext()
                     .getSessionMap().get("sessionUname").toString();
             
             //get the stocks that were purchased by the user
             
             String sql7 = "SELECT * from purchase where uname= '"+uname+"' ";
 			PreparedStatement st7 = connector.prepareStatement(sql7);
 			ResultSet rs = st7.executeQuery();
 			while(rs.next()) {
 				list.add(rs.getString("stock_symbol"));
 			}return list;
 			}catch (Exception e) {
				// TODO: handle exception
			} 	
    	 list.add("No purchased stocks");
    	return list;
    }
    
    // for sell stocks
    public String createDbRecord1(String symbol, double price, int qty, double amt) throws SQLException {
    	long quantity = 0;
    	
        	// if q > 0 from db ... q from front end - db 
    
        	connector = DBConnector.createConnector();
            Statement statement = connector.createStatement();
           
            String uname = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("sessionUname").toString();
         try {   
            String sql = "SELECT SUM(`qty`) FROM assignment5.purchase WHERE stock_symbol=? and uname=?;";
            //System.out.println(sql  + " is the quantity from sell controller");
            PreparedStatement st = connector.prepareStatement(sql);
            st.setString(1, symbol);
            st.setString(2, uname);
			ResultSet rs1 = st.executeQuery();
            while(rs1.next()) {
            	quantity = rs1.getLong(1);
            }
            System.out.println(quantity  + " is the quantity from sell controller ");
         }catch (SQLException e) {
			// TODO: handle exception
		}
  
        double availbleQuantity = quantity-qty;
        if(availbleQuantity>0) {
        	double user_balance=0, total_balance=0;
        	//adding to sell table
        	 try {   
                 String sql = "INSERT INTO sell(`stock_symbol`, `qty`,`price`,`amt`,`sold_by`) VALUES (?,?,?,?,?)";
                 
                 //System.out.println(sql  + " is the quantity from sell controller");
                 PreparedStatement st = connector.prepareStatement(sql);
                 st.setString(1, symbol);
                 st.setDouble(2, quantity);
                 st.setDouble(3, price);
                 st.setDouble(4,amt);
                 st.setString(5,uname);
                 st.executeUpdate();
                 System.out.println(" sell table inserted ");
              }catch (SQLException e) {
     			// TODO: handle exception
     		}
        	
        	 //get availible balance
        	 try {   
                 String sql = "SELECT balance FROM users WHERE uname=? ";
                 
                 //System.out.println(sql  + " is the quantity from sell controller");
                 PreparedStatement st = connector.prepareStatement(sql);
                 st.setString(1, uname);
                 ResultSet rs = st.executeQuery();
                 if(rs.next()) {
                	 user_balance = rs.getDouble("balance");
                 }
                 System.out.println(" got the availible balance ");
              }catch (SQLException e) {
     			// TODO: handle exception
     		}
        	 //get total balance
        	 total_balance = user_balance-amt;
        	 //upadate the balance and quantity 
        	 try {   
                 String sql = "UPDATE `purchase` p1,`users` u1 SET p1.qty=?, u1.balance=? WHERE p1.uname=? AND u1.uname=?";
                 
                 //System.out.println(sql  + " is the quantity from sell controller");
                 PreparedStatement st = connector.prepareStatement(sql);
                 st.setDouble(1, availbleQuantity);
                 st.setDouble(2, total_balance);
                 st.setString(3, uname);
                 st.setString(4, uname);
                 st.executeUpdate();
                 System.out.println(" updated purchase qty and user balance");
              }catch (SQLException e) {
     			// TODO: handle exception
     		}
        }
        
        
//           //get the stocks that were purchased by the user
//            List<String> list;
//            list = new ArrayList<String>();
//            
//            String sql7 = "SELECT * from purchase where uname= '"+uname+"' ";
//			PreparedStatement st7 = connector.prepareStatement(sql7);
//			ResultSet rs = st7.executeQuery();
//			while(rs.next()) {
//				String stock_symbol = rs.getString("stock_symbol");
//				String qty1 = rs.getString("qty");
//				String price1 = rs.getString("price");
//				String amt1 = rs.getString("amt");
//				String uname1 = rs.getString("uname");
//				
//				list.add(stock_symbol);
//				list.add(qty1);
//				list.add(price1);
//				list.add(amt1);
//				list.add(uname1);
//		
//				System.out.println(list); 
//				
//				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionlist",list);
//
//			}
//			viewPurchaseStocks(list);
//			st7.close();
//            
//            System.out.println(uname);
//            System.out.println("symbol:" + symbol);
//            System.out.println("price:" + price);
//            System.out.println("qty:" + qty);
//            System.out.println("amt:" + amt);
//            statement.executeUpdate("INSERT INTO `sell` (`stock_symbol`, `qty`, `price`, `amt`,`sold_by`) "
//                    + "VALUES (NULL,'" + symbol + "','" + qty + "','" + price + "','" + amt +"', '" + uname + "');");
//            
//            statement.close();
//            connector.close();
//            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock successfully sold",""));
//       
//    
        return "sellResult";
    }

    
    public String viewPurchaseStocks(List<String> list) {
		// System.out.println(list + "is the list from new view purchase");
    	System.out.println(list.get(qty)   + "is the quantity of the stock");
		installAllTrustingManager();
		
		return null;
    	
    }

   
    public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }

    public void timeseries() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/sellResult.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Sell Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }
 
  
    public void purchaseStock() {
        System.out.println("Calling function purchaseStocks()");
        System.out.println("stockSymbol: " + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockSymbol"));
        System.out.println("stockPrice" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockPrice"));
        return;
    }
    
    public void sellStock() {
//    	String sql7 = "insert into sell(stock_symbol,qty,price,amt,sessionUname,sold_by) values (?,?,?,?,?)";
        stockDAO s = new stockDAO();
		String uname = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUname");
		
        s.sellStockDAO(symbol,qty,price,amt,uname);
       
    }
}
