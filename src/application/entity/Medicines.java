package application.entity;

public class Medicines {

	private Integer codeMedicine;
	private Integer idMedicine;
	private String name;
	private String nameManufacture;
	private String unitMeasure;
	private Double quantity;
	
	public Integer getCodeMedicine() {
		return codeMedicine;
	}
	public void setCodeMedicine(Integer idMedicines) {
		this.codeMedicine = idMedicines;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameManufacture() {
		return nameManufacture;
	}
	public void setNameManufacture(String nameManufacture) {
		this.nameManufacture = nameManufacture;
	}
	public String getUnitMeasure() {
		return unitMeasure;
	}
	public void setUnitMeasure(String unitMeasure) {
		this.unitMeasure = unitMeasure;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Integer getIdMedicine() {
		return idMedicine;
	}
	public void setIdMedicine(Integer keyID) {
		this.idMedicine = keyID;
	}
}
