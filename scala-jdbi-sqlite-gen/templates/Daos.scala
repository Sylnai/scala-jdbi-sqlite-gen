package moe.pizza.sdd

import moe.pizza.sdd.Types._
import moe.pizza.sdd.Mappers._
import org.skife.jdbi.v2.sqlobject.{Bind, SqlQuery}
import org.skife.jdbi.v2.sqlobject.customizers.Mapper

abstract class Daos extends Dao {

  {%- for table in tables %}
    // methods for table {{ table["name"] }}
    {%- for entry in table["entries"] %}
      // methods for column {{ entry[0] }} of type {{ entry[1] }}

      @Mapper(classOf[{{ table["name"] }}Mapper])
      @SqlQuery("SELECT * from {{ table["name"] }} where {{ entry[0] }}=:{{ entry[0] }}")
      def getFrom{{ table["name"] }}by{{ entry[0] }}(@Bind("{{ entry[0] }}") {{ entry[0] }}: {{ entry[1] }}): {{ table["name"] }}Entry

      {%- if entry[1] == "String" %}
      @Mapper(classOf[{{ table["name"] }}Mapper])
      @SqlQuery("SELECT * from {{ table["name"] }} where {{ entry[0] }} like :{{ entry[0] }}")
      def getFrom{{ table["name"] }}byLike{{ entry[0] }}(@Bind("{{ entry[0]}}") {{ entry[0] }}: {{ entry[1] }}): {{ table["name"] }}Entry
      {% endif %}

    {%- endfor %}

  {%- endfor %}

}
