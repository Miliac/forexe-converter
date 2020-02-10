import xmlschema

my_schema = xmlschema.XMLSchema('bal_verif_ f1102_v0.xsd')

my_schema.validate('f1102.xml')


