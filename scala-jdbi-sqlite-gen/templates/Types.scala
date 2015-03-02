package moe.pizza.sdd

object Types {
  {% for table in tables %}
  case class {{ table["name"] }}Entry (
    {%- for entry in table["entries"] %}
      {{ entry[0] }}: {{ entry[1] }}{% if not loop.last %},{% endif %}
    {%- endfor %}
    )
  {% endfor %}
}
