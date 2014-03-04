package com.spectral.cc.core.directory.base.model.organisational;

import com.spectral.cc.core.directory.base.model.technical.system.OSInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@XmlRootElement
@Table(name="team",uniqueConstraints = @UniqueConstraint(columnNames = {"teamName","teamCC"}))
public class Team implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Team.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;
    @Version
    @Column(name = "version")
    private int version = 0;

    @Column(name="teamName",unique=true)
    @NotNull
    private String name;

    @Column
    private String description;

    @Column(name="teamCC",unique=true)
    @NotNull
    private String colorCode;

    @ManyToMany
    private Set<OSInstance> osInstances = new HashSet<OSInstance>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<Application>();

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Team setIdR(final Long id) {
        this.id = id;
        return this;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Team setVersionR(final int version) {
        this.version = version;
        return this;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        if (id != null) {
            return id.equals(((Team) that).id);
        }
        return super.equals(that);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Team setNameR(final String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Team setDescriptionR(final String description) {
        this.description = description;
        return this;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Team setColorCodeR(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (name != null && !name.trim().isEmpty())
            result += "name: " + name;
        if (description != null && !description.trim().isEmpty())
            result += ", description: " + description;
        return result;
    }

    public Set<OSInstance> getOsInstances() {
        return this.osInstances;
    }

    public void setOsInstances(final Set<OSInstance> osInstances) {
        this.osInstances = osInstances;
    }

    public Team setOsInstancesR(final Set<OSInstance> osInstances) {
        this.osInstances = osInstances;
        return this;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Team setApplicationsR(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Team clone() {
        return new Team().setIdR(id).setVersionR(version).setNameR(name).setDescriptionR(description).setOsInstancesR(new HashSet<>(osInstances)).
                          setApplicationsR(new HashSet<Application>(applications)).setColorCodeR(colorCode);
    }

    public final static String TEAM_SUPPORT_MAPPING_PROPERTIES = "supportTeam";
    private final static String TEAM_NAME_MAPPING_FIELD = "name";
    private final static String TEAM_COLR_MAPPING_FIELD = "color";

    public HashMap<String,Object> toMappingProperties() {
        HashMap<String,Object> ret = new HashMap<String,Object>();
        ret.put(TEAM_NAME_MAPPING_FIELD,name);
        ret.put(TEAM_COLR_MAPPING_FIELD,colorCode);
        return ret;
    }
}