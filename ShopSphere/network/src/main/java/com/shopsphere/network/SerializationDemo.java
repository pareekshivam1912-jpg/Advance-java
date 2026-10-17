package com.shopsphere.network;

import com.shopsphere.model.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

/**
 * RTU Advanced Java Syllabus Practical Demonstration:
 * Java Object Serialization and Deserialization (java.io.Serializable)
 *
 * Core Concepts Demonstrated:
 * 1. ObjectOutputStream (write_object) / ObjectInputStream (read_object)
 * 2. Role of 'transient' keyword (omitted from serialized byte stream)
 * 3. Role of 'serialVersionUID' in class evolution and version verification
 * 4. What happens when class definitions change (InvalidClassException)
 */
public class SerializationDemo {

    private static final String FILE_NAME = "product.ser";

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: SERIALIZATION DEMONSTRATION   ");
        System.out.println("==========================================================================");

        // 1. Instantiate sample Product domain model
        Product originalProduct = new Product();
        originalProduct.setId(101);
        originalProduct.setCategoryId(1);
        originalProduct.setCategoryName("Electronics");
        originalProduct.setName("Sony WH-1000XM5 Wireless Headphones");
        originalProduct.setBrand("Sony");
        originalProduct.setDescription("Industry-leading active noise canceling wireless headphones");
        originalProduct.setPrice(new BigDecimal("29990.00"));
        originalProduct.setDiscountPercentage(new BigDecimal("10.00"));
        originalProduct.setStock(25);
        originalProduct.setStatus("ACTIVE");

        // Set the transient field (simulating temporary in-memory session/cache token)
        originalProduct.setSessionViewToken("TEMP_CACHE_SESSION_XYZ_98765");

        System.out.println("\n[1] ORIGINAL OBJECT BEFORE SERIALIZATION:");
        System.out.println("    - ID:                " + originalProduct.getId());
        System.out.println("    - Name:              " + originalProduct.getName());
        System.out.println("    - Brand:             " + originalProduct.getBrand());
        System.out.println("    - Price:             Rs. " + originalProduct.getPrice());
        System.out.println("    - Discounted Price:  Rs. " + originalProduct.getDiscountedPrice());
        System.out.println("    - Stock:             " + originalProduct.getStock());
        System.out.println("    - Transient Token:   " + originalProduct.getSessionViewToken() + "  <-- (Should NOT persist!)");

        // 2. Serialize object to product.ser
        File file = new File(FILE_NAME);
        System.out.println("\n[2] SERIALIZING OBJECT TO FILE: " + file.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(originalProduct);
            oos.flush();
            System.out.println("    >>> Successfully serialized Product object. File size: " + file.length() + " bytes.");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3. Deserialize object back from product.ser
        System.out.println("\n[3] DESERIALIZING OBJECT BACK FROM FILE: " + file.getAbsolutePath());
        Product deserializedProduct = null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            deserializedProduct = (Product) ois.readObject();
            System.out.println("    >>> Successfully deserialized Product object from binary stream.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 4. Verify round trip and transient behavior
        System.out.println("\n[4] RESTORED OBJECT AFTER DESERIALIZATION:");
        System.out.println("    - ID:                " + deserializedProduct.getId());
        System.out.println("    - Name:              " + deserializedProduct.getName());
        System.out.println("    - Brand:             " + deserializedProduct.getBrand());
        System.out.println("    - Price:             Rs. " + deserializedProduct.getPrice());
        System.out.println("    - Discounted Price:  Rs. " + deserializedProduct.getDiscountedPrice());
        System.out.println("    - Stock:             " + deserializedProduct.getStock());
        System.out.println("    - Transient Token:   " + deserializedProduct.getSessionViewToken() + "  <-- (VERIFIED: null as expected!)");

        // 5. Verification checks
        boolean stateMatch = originalProduct.getId() == deserializedProduct.getId()
                && originalProduct.getName().equals(deserializedProduct.getName())
                && originalProduct.getPrice().compareTo(deserializedProduct.getPrice()) == 0;

        boolean transientNull = (deserializedProduct.getSessionViewToken() == null);

        System.out.println("\n[5] VERIFICATION SUMMARY:");
        System.out.println("    - State Match Test:     " + (stateMatch ? "PASSED (Data intact)" : "FAILED"));
        System.out.println("    - Transient Field Test: " + (transientNull ? "PASSED (Omitted from stream)" : "FAILED"));

        // 6. Academic / Viva theory summary
        printSyllabusExplanation();

        // Cleanup temporary serialization artifact
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("\n[Cleanup] Temporary demo file '" + FILE_NAME + "' deleted: " + deleted);
        }
    }

    private static void printSyllabusExplanation() {
        System.out.println("\n==========================================================================");
        System.out.println("   VIVA & SYLLABUS REFERENCE: SERIALIZATION & serialVersionUID CONCEPTS   ");
        System.out.println("==========================================================================");
        System.out.println("Q: What is Serialization in Java?");
        System.out.println("A: Serialization is the process of converting an object's state into a byte");
        System.out.println("   stream to store it in a file, database, or send across a network (Socket/RMI).");
        System.out.println("   Classes must implement the 'java.io.Serializable' marker interface.\n");
        System.out.println("Q: What does the 'transient' keyword do?");
        System.out.println("A: Fields marked 'transient' are skipped during serialization. Upon deserialization,");
        System.out.println("   they default to null (references) or 0/false (primitives). Used for passwords,");
        System.out.println("   runtime caches, DB connections, or thread locks.\n");
        System.out.println("Q: What is 'serialVersionUID' and what happens when a class changes?");
        System.out.println("A: 'serialVersionUID' is a unique universal identifier used during deserialization");
        System.out.println("   to ensure sender and receiver have loaded compatible classes.");
        System.out.println("   - If the sender serializes an object with UID 1L, but the class on the receiver");
        System.out.println("     has UID 2L (or the default hash changed because a field was added/removed),");
        System.out.println("     the JVM throws 'java.io.InvalidClassException'.");
        System.out.println("   - Explicitly defining 'private static final long serialVersionUID = 1L;'");
        System.out.println("     ensures backward compatibility across minor class modifications.");
        System.out.println("==========================================================================");
    }
}
