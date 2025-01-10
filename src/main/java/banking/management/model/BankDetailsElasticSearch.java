package banking.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "bank_master")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDetailsElasticSearch {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String ifsc;

    @Field(type = FieldType.Text)
    private String branch;

    @Field(type = FieldType.Text)
    private String micr;

    @Field(type = FieldType.Text)
    private String contact;

    @Field(type = FieldType.Boolean)
    private boolean upi;

    @Field(type = FieldType.Boolean)
    private boolean rtgs;

    @Field(type = FieldType.Boolean)
    private boolean neft;

    @Field(type = FieldType.Boolean)
    private boolean imps;

    @Field(type = FieldType.Text)
    private String swift;

    @Field(type = FieldType.Text)
    private String iso3166;

    @Field(type = FieldType.Text)
    private String bank;

    @Field(type = FieldType.Text)
    private String bankCode;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String centre;

    @Field(type = FieldType.Text)
    private String state;

    @Field(type = FieldType.Text)
    private String district;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt;

//    private String createdAt;
//
//    private String updatedAt;
}
