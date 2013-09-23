package com.logbookmanager.domain.model.logbook;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.logbookmanager.domain.model.person.PersonLogbook;
import com.logbookmanager.domain.support.EntitySupport;
import com.logbookmanager.domain.support.Title;

/**
 */
@Entity
@Table(name = "Logbook")
public class Logbook extends EntitySupport<Logbook, Long> implements
		java.io.Serializable {
	private static final long serialVersionUID = 912839123L;

	private Title title;

	private Set<LogbookPage> logbookPages = new HashSet<LogbookPage>();

	// a set of people this logbook belongs to
	private Set<PersonLogbook> personLogbooks = new HashSet<PersonLogbook>();

	/** default constructor required by Hibernate */
	Logbook() {
	}

	public Logbook(Title title) {
		Validate.notNull(title);
		this.setTitle(title);
	}

	/**
	 * 
	 */
	public Set<LogbookPage> getLogbookPages() {
		return this.logbookPages;
	}

	public void setLogbookPages(Set<LogbookPage> logbookPages) {
		this.logbookPages = logbookPages;
	}

	/**
	 * 
	 */
	public Set<PersonLogbook> getPersonLogbooks() {
		return this.personLogbooks;
	}

	public void setPersonLogbooks(Set<PersonLogbook> personLogbooks) {
		this.personLogbooks = personLogbooks;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Logbook)) {
			return false;
		}
		Logbook rhs = (Logbook) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.version, rhs.version).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(-421608611, -181807385).append(this.id).append((title == null)?5:title.hashCode())
				.append(this.version).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("title",title.toString())
				.append("version", this.version).toString();
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

}