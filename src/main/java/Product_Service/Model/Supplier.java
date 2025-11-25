//package Product_Service.Model;
//
//import jakarta.persistence.*;
//
//import java.util.List;
//import java.util.UUID;
//@Entity
//@Table(name = "suppliers")
//public class Supplier {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID supplierId;
//    @Column(name = "supplier_name", nullable = false)
//    private String supplierName;
//
//    @Column(name = "supplier_address", nullable = false)
//    private String supplierAddress;
//
//    @Column(name = "supplier_contact", nullable = false)
//    private String supplierContact;
//
//    @Column(name = "supplier_email", nullable = false)
//    @OneToMany(mappedBy = "supplier")
//    private List<Product> products;
//
//}
