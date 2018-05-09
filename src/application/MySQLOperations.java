package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import application.entity.ActionTable;
import application.entity.Medicines;
import application.entity.OrderPlaced;
import application.entity.ReportsTable;
import application.entity.StatePharmacyTable;
import application.entity.Supplier;
import application.entity.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class MySQLOperations {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String url = "jdbc:mysql://localhost/apoteka?"
            + "user=root";
	
	static {
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
    public User findUser(String logUsername) throws Exception {
        try {
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String findUserSQL = "select * from apoteka.korisnik where korisnicko_ime = ?";
            preparedStatement = connect.prepareStatement(findUserSQL);
            preparedStatement.setString(1, logUsername);
            resultSet = preparedStatement
                    .executeQuery();
            connect.commit();
            return userResultSet(resultSet);
        } catch (Exception e) {
        	connect.rollback();
            throw e;
        } finally {
            close();
        }
    }
    
    public void createUser(User newUser) throws Exception{
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String createUserSQL = " insert into apoteka.korisnik (tip, korisnicko_ime, lozinka, ime, prezime, jmbg) values (?, ?, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(createUserSQL);
            preparedStatement.setBoolean(1, newUser.isTip());
            preparedStatement.setString(2, newUser.getUsername());
            preparedStatement.setString(3, newUser.getHashPass());
            preparedStatement.setString(4, newUser.getName());
            preparedStatement.setString(5, newUser.getSurname());
            preparedStatement.setString(6, newUser.getJmbg());
            preparedStatement.executeUpdate();
            connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}
    }

    private User userResultSet(ResultSet rs) throws SQLException {
    	User user = null;
    	while (rs.next()) {
    		user = new User();
            user.setUsername(resultSet.getString("korisnicko_ime"));
            user.setName(resultSet.getString("ime"));
            user.setSurname(resultSet.getString("prezime"));
            user.setHashPass(resultSet.getString("lozinka"));
            user.setJmbg(resultSet.getString("jmbg"));
            user.setTip(resultSet.getBoolean("tip"));
            user.setIdPerson(Integer.parseInt(resultSet.getString("key_korisnik")));
        }
		return user;
    }

	public Medicines findMedicine(String strIdMed) throws Exception {
        try {
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String findMedSQL = "select * from apoteka.stanje_lekova where sifra_leka = ?";
            preparedStatement = connect.prepareStatement(findMedSQL);
            preparedStatement.setString(1, strIdMed);
            resultSet = preparedStatement
                    .executeQuery();
            connect.commit();
            return medResultSet(resultSet);
        } catch (Exception e) {
        	connect.rollback();
            throw e;
        } finally {
            close();
        }
	}
	
	private Medicines medResultSet(ResultSet rs) throws NumberFormatException, SQLException {
		Medicines med = null;
    	while (rs.next()) {
    		med = new Medicines();
    		med.setIdMedicine(Integer.parseInt(resultSet.getString("key_lek")));
    		med.setCodeMedicine(Integer.parseInt(resultSet.getString("sifra_leka")));
    		med.setName(resultSet.getString("naziv"));
    		med.setNameManufacture(resultSet.getString("proizvodjac"));
    		med.setQuantity(Double.parseDouble(resultSet.getString("kolicina")));
    		med.setUnitMeasure(resultSet.getString("jedinica_mere"));
        }
		return med;
	}

	public void createMedicine(Medicines med) throws Exception {
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String createUserSQL = " insert into apoteka.stanje_lekova (naziv, sifra_leka, proizvodjac, jedinica_mere, 	kolicina) values (?, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(createUserSQL);
            preparedStatement.setString(1, med.getName());
            preparedStatement.setInt(2, med.getCodeMedicine());
            preparedStatement.setString(3, med.getNameManufacture());
            preparedStatement.setString(4, med.getUnitMeasure());
            preparedStatement.setDouble(5, med.getQuantity());
            preparedStatement.executeUpdate();
            connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();    		
		}
		
	}
	
	public Supplier findSupplier(String strNameSup) throws Exception {
        try {
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String findSupSQL = "select * from apoteka.dobavljac where naziv = ?";
            preparedStatement = connect.prepareStatement(findSupSQL);
            preparedStatement.setString(1, strNameSup);
            resultSet = preparedStatement
                    .executeQuery();
            connect.commit();
            return supResultSet(resultSet);
        } catch (Exception e) {
        	connect.rollback();
            throw e;
        } finally {
            close();
        }
	}
	
	private Supplier supResultSet(ResultSet rs) throws SQLException {
		Supplier sup = null;
    	while (rs.next()) {
    		sup = new Supplier();
    		sup.setIdSupplier(Integer.parseInt(rs.getString("key_dobavljac")));
    		sup.setName(rs.getString("naziv"));
        }
    	return sup;
	}

	public void createSupplier(Supplier sup) throws Exception {
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String createUserSQL = " insert into apoteka.dobavljac (naziv) values (?)";
            preparedStatement = connect.prepareStatement(createUserSQL);
            preparedStatement.setString(1, sup.getName());
            preparedStatement.executeUpdate();
            connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}
	}
	public void searchReports(String sqlSearch, long dF, long dT, String strSearch, int mode, ObservableList<ReportsTable> dataReportsT, TableView<ReportsTable> reportsTView) throws Exception{
		 try {
			connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
			preparedStatement = connect.prepareStatement(sqlSearch);
			if(mode == 0){
				if(strSearch.length() > 0){
					preparedStatement.setString(1, strSearch + "%");
					preparedStatement.setString(2, strSearch + "%");
				}
			}else{
				preparedStatement.setTimestamp(1, new Timestamp(dF));
				preparedStatement.setTimestamp(2, new Timestamp(dT));
				if(strSearch.length() > 0){
					preparedStatement.setString(3, strSearch + "%");
					preparedStatement.setString(4, strSearch + "%");
				}
			}
			resultSet = preparedStatement.executeQuery();
			createReportsTableView(resultSet, dataReportsT);
			reportsTView.setItems(dataReportsT);
			connect.commit();
		} catch (Exception e) {
			connect.rollback();
            throw e;
		} finally {
    		close();
		}
	}
	
	private void createReportsTableView(ResultSet rs, ObservableList<ReportsTable> dataTab) throws Exception {
    	try {
			while (rs.next()) {
				ReportsTable entry = new ReportsTable(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6));
				dataTab.add(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}
	
	public void searchState(String strSearch, String sqlSearch, ObservableList<StatePharmacyTable> dataStateT,
			TableView<StatePharmacyTable> stateTableV) throws Exception {
		 try {
			connect = DriverManager
			         .getConnection(url);
            connect.setAutoCommit(false);
			preparedStatement = connect.prepareStatement(sqlSearch);
			if(strSearch.length() > 0){
				preparedStatement.setString(1, strSearch + "%");
				preparedStatement.setString(2, strSearch + "%");
			}
			resultSet = preparedStatement.executeQuery();
			createStateTableView(resultSet, dataStateT);
			stateTableV.setItems(dataStateT);
			connect.commit();
		} catch (Exception e) {
			connect.rollback();
			throw e;
		} finally {
			close();
		}
		
	}
	
	private void createStateTableView(ResultSet rs, ObservableList<StatePharmacyTable> dataStateT) throws Exception {
    	try {
			while (rs.next()) {
				StatePharmacyTable entry = new StatePharmacyTable(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
				dataStateT.add(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}
	
	public void createOrder(OrderPlaced op) throws Exception{
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String createUserSQL = " insert into apoteka.narucivanje_leka (key_lek, key_korisnik, key_dobavljac, datum_narucivanja, kolicina) values (?, ?, ?, ?, ?)";
            preparedStatement = connect.prepareStatement(createUserSQL);
            preparedStatement.setInt(1, op.getIdMedicines());
            preparedStatement.setInt(2, op.getIdPerson());
            preparedStatement.setInt(3, op.getIdSupplier());
            preparedStatement.setTimestamp(4, op.getDateOrdering());
            preparedStatement.setDouble(5, op.getQuantity());

            preparedStatement.executeUpdate();
            connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}
	}

	public void deactiveMed(List<Integer> listDeleteKeys) throws Exception {
		try{
		   connect = DriverManager.getConnection(url);
           connect.setAutoCommit(false);
		   String deactiveSQL = " delete from apoteka.stanje_lekova where key_lek = ?";
		   preparedStatement = connect.prepareStatement(deactiveSQL);
		   for(Integer keyM : listDeleteKeys){
			   preparedStatement.setInt(1, keyM);
			   preparedStatement.addBatch();
		   }
		   preparedStatement.executeBatch();
		   connect.commit();
		} catch(Exception e){
			connect.rollback();
			throw e;
		} finally {
			close();
		}
	}
	public void createOrderForMed(List<ActionTable> listMedicine, Integer keyUser) throws Exception {
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
        	String createOrderSQL = " insert into apoteka.narucivanje_leka (key_lek, kolicina, key_dobavljac, key_korisnik) values (?, ?, ?, ?)";
        	preparedStatement = connect.prepareStatement(createOrderSQL);

            for(ActionTable at : listMedicine){
            	preparedStatement.setInt(1, at.getStateMedicine().getIdMedicine());
            	preparedStatement.setDouble(2, Double.valueOf(at.actionCapProperty().getValue()));
            	preparedStatement.setInt(3,at.getSup().getIdSupplier());
            	preparedStatement.setInt(4, keyUser);

            	preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        	connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}
	}
	
	public void createIssuingForMed(List<ActionTable> listMedicine, Integer keyUser) throws Exception {
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
        	String createOrderSQL = " insert into apoteka.izdavanje_leka (key_lek, kolicina, key_korisnik) values (?, ?, ?)";
        	preparedStatement = connect.prepareStatement(createOrderSQL);

            for(ActionTable at : listMedicine){
            	preparedStatement.setInt(1, at.getStateMedicine().getIdMedicine());
            	preparedStatement.setDouble(2, Double.valueOf(at.actionCapProperty().getValue()));
            	preparedStatement.setInt(3, keyUser);
            	preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        	connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}		
	}
	
	public void updateMedAction(List<ActionTable> listMedicine, Boolean isModeIssuing) throws Exception{
    	try{
            connect = DriverManager.getConnection(url);
            connect.setAutoCommit(false);
            String createOrderUpSQL = " update apoteka.stanje_lekova set kolicina = ? where key_lek = ?";
            preparedStatement = connect.prepareStatement(createOrderUpSQL);
            if(!isModeIssuing){
	            for(ActionTable at : listMedicine){
	            	preparedStatement.setDouble(1, Double.valueOf(at.actionCapProperty().getValue()) + at.getStateMedicine().getStateCap());
	            	preparedStatement.setInt(2, at.getStateMedicine().getIdMedicine());
	            	preparedStatement.addBatch();
	            }
            } else {
	            for(ActionTable at : listMedicine){
	            	preparedStatement.setDouble(1, at.getStateMedicine().getStateCap() -  Double.valueOf(at.actionCapProperty().getValue()));
	            	preparedStatement.setInt(2, at.getStateMedicine().getIdMedicine());
	            	preparedStatement.addBatch();
	            }
            }
            preparedStatement.executeBatch();
        	connect.commit();
    	} catch(Exception e){
    		connect.rollback();
            throw e;
    	} finally {
    		close();
		}
	}
	
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
   }
}
