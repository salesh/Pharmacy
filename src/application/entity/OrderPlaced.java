package application.entity;

import java.sql.Timestamp;

public class OrderPlaced {
	
	private Integer idOrderPlaced;
	private Integer idMedicines;
	private Integer idPerson;
	private Integer idSupplier;
	private Timestamp dateOrdering;
	private Double quantity;
	
	public Integer getIdOrderPlaced() {
		return idOrderPlaced;
	}
	public void setIdOrderPlaced(Integer idOrderPlaced) {
		this.idOrderPlaced = idOrderPlaced;
	}
	public Integer getIdMedicines() {
		return idMedicines;
	}
	public void setIdMedicines(Integer idMedicines) {
		this.idMedicines = idMedicines;
	}
	public Integer getIdPerson() {
		return idPerson;
	}
	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}
	public Integer getIdSupplier() {
		return idSupplier;
	}
	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier = idSupplier;
	}
	public Timestamp getDateOrdering() {
		return dateOrdering;
	}
	public void setDateOrdering(Timestamp dateOrdering) {
		this.dateOrdering = dateOrdering;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
}
