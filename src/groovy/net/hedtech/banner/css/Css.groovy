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
		  """
        ),
        @NamedQuery(name = "Css.fetchAllByConstantNameLike",
                query = """FROM   Css a
		   WHERE  a.constantName like :constantName
		  """)
])
class Css implements Serializable{

    @Id
    @Column(name="ID")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = 'CSS_SEQ_GENERATOR', sequenceName = 'SSPBMGR.HIBERNATE_SEQUENCE')
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 'CSS_SEQ_GENERATOR')
    Long id;

    @Column(name="CONSTANT_NAME", nullable= false ,length = 60)
    String constantName

    @Column(name="CSS", nullable = false)
    @Lob
    String css

    @Column(name="DESCRIPTION" ,nullable = true , length = 255)
    String description

    @Version
    @Column(name="VERSION" , nullable = false, precision = 19)
    Long version

    @Column(name = "DATE_CREATED",  nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated

    @Column(name = "LAST_UPDATED",  nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdated

    @Column(name = "FILE_TIMESTAMP", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date fileTimestamp

    @Transient
    String lastModifiedBy // Transient to work around banner-core issue

    @Override
    public String toString() {
        return "Css{" +
                "id=" + id +
                ", constantName='" + constantName + '\'' +
                ", css='" + css + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", fileTimestamp='" + fileTimestamp + '\'' +
                ", lastModifiedBy=" + lastModifiedBy +
                '}';
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Css that = (Css) o

        if (constantName != that.constantName) return false
        if (id != that.id) return false
        if (css != that.css) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (description != that.description) return false
        if (dateCreated != that.dateCreated) return false
        if (lastUpdated != that.lastUpdated) return false
        if (version != that.version) return false
        if (fileTimestamp != that.fileTimestamp) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (constantName != null ? constantName.hashCode() : 0)
        result = 31 * result + (css != null ? css.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
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

    public static Css fetchAllByConstantNameLike(String constantName) {
        List css = []
        css = Css.withSession {session ->
            css = session.getNamedQuery('Css.fetchAllByConstantNameLike').setString('constantName', constantName).list()}
        Css result = css?.size()>0?css.get(0):null
        return result
    }
}