package finalProj.domain.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendor")
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Vendor_ID")
	private Integer VendorID;// 廠商 ID

	@Column(name = "vendor_name")
	private String vendorName;// 廠商名稱

	@Column(name = "contact_person")
	private String contactPerson;// 聯絡人姓名

	@Column(name = "phone_number")
	private String phoneNumber;// 聯絡電話

	@Column(name = "address")
	private String address;// 地址

	@Column(name = "notes")
	private String notes;// 其他備註

	@Column(name = "tax_ID_number")
	private Integer taxIDNumber;// 廠商統編

	@Override
	public String toString() {
		return "Vendor [VendorID=" + VendorID + ", vendorName=" + vendorName + ", contactPerson=" + contactPerson
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", notes=" + notes + ", taxIDNumber="
				+ taxIDNumber + "]";
	}

	public Integer getVendorID() {
		return VendorID;
	}

	public void setVendorID(Integer vendorID) {
		VendorID = vendorID;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getTaxIDNumber() {
		return taxIDNumber;
	}

	public void setTaxIDNumber(Integer taxIDNumber) {
		this.taxIDNumber = taxIDNumber;
	}

}