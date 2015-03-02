package moe.pizza.sdd

import java.sql.ResultSet

import moe.pizza.sdd.Types._
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

object Mappers {
  {%- for table in tables %}
  class {{ table["name"] }}Mapper extends ResultSetMapper[{{ table["name"] }}Entry] {
    override def map(i: Int, rs: ResultSet, statementContext: StatementContext): {{ table["name"] }}Entry = {
      new {{ table["name"] }}Entry(
      {%- for entry in table["entries"] %}
        rs get{{ entry[1]}} "{{ entry[0] }}"{% if not loop.last %},{% endif %}
      {%- endfor %}
      )
    }
  }
  {% endfor %}
}
