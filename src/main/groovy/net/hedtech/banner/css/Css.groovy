/*******************************************************************************
 Copyright 2017-2019 Ellucian Company L.P. and its affiliates.
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
		  """),
        @NamedQuery(name = "Css.fetchAllByCssOwnerLike",
                query = """FROM   Css a
		   WHERE  a.owner like :owner
		  """),
        @NamedQuery(name = "Css.fetchById",
                query = """FROM   Css a
		   WHERE  a.id = :id
		  """
        )
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

    @Column(name="CSS_OWNER", nullable= true ,length = 30)
    String owner

    @Column(name="CSS_ALLOW_ALL_IND", nullable= true ,length = 1)
    String allowAllInd

    @Column(name="CSS_TAG", nullable= true ,length = 60)
    String tag

    @Transient
    String lastModifiedBy // Transient to work around banner-core issue


    static constraints = {
        tag(nullable:true, maxSize:60)
        allowAllInd(nullable:true, maxSize:1)
        owner(nullable:true, maxSize:30)
        description(nullable:true, maxSize:255)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Css css1 = (Css) o

        if (constantName != css1.constantName) return false
        if (css != css1.css) return false
        if (allowAllInd != css1.allowAllInd) return false
        if (owner != css1.owner) return false
        if (tag != css1.tag) return false
        if (dateCreated != css1.dateCreated) return false
        if (description != css1.description) return false
        if (fileTimestamp != css1.fileTimestamp) return false
        if (id != css1.id) return false
        if (lastModifiedBy != css1.lastModifiedBy) return false
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
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0)
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0)
        result = 31 * result + (fileTimestamp != null ? fileTimestamp.hashCode() : 0)
        result = 31 * result + (owner != null ? owner.hashCode() : 0)
        result = 31 * result + (allowAllInd != null ? allowAllInd.hashCode() : 0)
        result = 31 * result + (tag != null ? tag.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
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

    public static Css fetchAllByCSSOwnerLike(String owner) {
        List css = []
        css = Css.withSession {session ->
            css = session.getNamedQuery('Css.fetchAllByCssOwnerLike').setString('owner', owner).list()}
        Css result = css?.size()>0?css.get(0):null
        return result
    }

    public static Css findById(String id) {
        List css = []
        css = Css.withSession {session ->
            css = session.getNamedQuery('Css.fetchById').setString('id', id).list()}
        Css result = css?.size()>0?css.get(0):null
        return result
    }

    @Override
    public String toString() {
        return "Css{" +
                "id=" + id +
                ", constantName='" + constantName + '\'' +
                ", css='" + css + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", fileTimestamp=" + fileTimestamp +
                ", cssOwner='" + owner + '\'' +
                ", cssAllowAll='" + allowAllInd + '\'' +
                ", cssTag='" + tag + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}