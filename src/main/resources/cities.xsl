<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/XSL/Transform" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
<!--    отрезать все пробелы-->
    <xsl:strip-space elements="*"/>
    <xsl:template match="/Payload/Cities/City/text()">
<!--        точка означает "сам себя"-->
        <xsl:copy-of select="."/>
<!--        put in new line-->
        <xsl:text>&#xa;</xsl:text>
    </xsl:template>
<!--    Чтобы выводить только города-->
<!--    ВНИМАНИЕ /Payload/Cities/City/text() не верно, поэтому text() не выведется-->
    <xsl:template match="text()"/>
</xsl:stylesheet>