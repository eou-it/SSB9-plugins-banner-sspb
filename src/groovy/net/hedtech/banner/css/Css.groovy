/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.css

import javax.persistence.*

@Entity
@Table(name = "CSS")
@NamedQueries(value = [
        @NamedQuery(name = "Css.fetchByConstantName",
                query = """FROM   Css a
		   WHERE  a.constantName = :constantName
		  """)
])
class Css implements Serializable{

    @Id
    @Column(name="ID")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = 'CSS_SEQ_GENERATOR', sequenceName = 'HIBERNATE_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'CSS_SEQ_GENERATOR')
    Long id;

    @Column(name="CONSTANT_NAME",nullable= false ,length =  60)
    String constantName

    @Column(name="CSS",nullable = false)
    @Lob
    String css

    @Column(name="DESCRIPTION" ,nullable = true , length =  255)
    String description

    @Column(name="DATE_CREATED",nullable=true)
    Date dateCreated

    @Column(name="LAST_UPDATED",nullable=true)
    Date lastUpdated

    @Column(name="FILE_TIMESTAMP",nullable=true)
    Date fileTimestamp

    @Version
    @Column(name="VERSION" , nullable = false, precision = 19)
    Long version

    @Override
    public String toString() {
        return "Css{" +
                "id=" + id +
                ", constantName='" + constantName + '\'' +
                ", css='" + css + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", fileTimestamp=" + fileTimestamp +
                ", version=" + version +
                '}';
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Css)) return false

        Css css1 = (Css) o

        if (constantName != css1.constantName) return false
        if (css != css1.css) return false
        if (dateCreated != css1.dateCreated) return false
        if (description != css1.description) return false
        if (fileTimestamp != css1.fileTimestamp) return false
        if (id != css1.id) return false
        if (lastUpdated != css1.lastUpdated) return false
        if (version != css1.version) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (constantName != null ? constantName.hashCode() : 0)
        result = 31 * result + (css != null ? css.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0)
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0)
        result = 31 * result + (fileTimestamp != null ? fileTimestamp.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        return result
    }

    public static Css fetchByConstantName(String constantName) {
        List css = []
        css = Css.withSession {session ->
            css = session.getNamedQuery('Css.fetchByConstantName').setString('constantName',constantName).list()}
        Css result = css?.size()>0?css.get(0):null
        return result
    }
}