<?xml version="1.0" encoding="UTF-8"?>
<tileset name="dungeon_tiles" tilewidth="16" tileheight="16" tilecount="240" columns="15">
 <properties>
  <property name="defaultTile" type="int" value="144"/>
 </properties>
 <image source="dungeon_tiles.png" trans="ffffff" width="240" height="256"/>
 <terraintypes>
  <terrain name="Floor" tile="48"/>
  <terrain name="Rocky Floor" tile="48"/>
  <terrain name="Water" tile="48"/>
 </terraintypes>
 <tile id="0" type="Floor" terrain=",,,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="0" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="1" type="Floor" terrain=",,0,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
  </objectgroup>
 </tile>
 <tile id="2" type="Floor" terrain=",,0," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="3" type="Floor" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="14" width="2" height="2"/>
   <object id="2" x="14" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="4" type="Floor" terrain="0,0,0," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="14" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="5" type="Floor" terrain="0,0,,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="6" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="0" width="16" height="2"/>
   <object id="3" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="7" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="0" width="16" height="2"/>
   <object id="3" x="14" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="8" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="10" type="Water" terrain=",,,2">
  <properties>
   <property name="collisionInherit" type="int" value="0"/>
  </properties>
 </tile>
 <tile id="11" type="Water" terrain=",,2,2">
  <properties>
   <property name="collisionInherit" type="int" value="1"/>
  </properties>
 </tile>
 <tile id="12" type="Water" terrain=",,2,">
  <properties>
   <property name="collisionInherit" type="int" value="2"/>
  </properties>
 </tile>
 <tile id="15" type="Floor" terrain=",0,,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="16" type="Floor" terrain="0,0,0,0" probability="25"/>
 <tile id="17" type="Floor" terrain="0,,0," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="18" type="Floor" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="2"/>
   <object id="2" x="14" y="0" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="19" type="Floor" terrain="0,,0,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="14" y="0" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="20" type="Floor" terrain=",0,0,0" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2"/>
  </objectgroup>
 </tile>
 <tile id="21" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="22" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
   <object id="3" x="14" y="0" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="23" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="2"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
   <object id="3" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="24" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="0" width="16" height="2"/>
   <object id="3" x="0" y="14" width="16" height="2"/>
   <object id="4" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="25" type="Water" terrain=",2,,2">
  <properties>
   <property name="collisionInherit" type="int" value="15"/>
  </properties>
 </tile>
 <tile id="26" type="Water" terrain="2,2,2,2"/>
 <tile id="27" type="Water" terrain="2,,2,">
  <properties>
   <property name="collisionInherit" type="int" value="17"/>
  </properties>
 </tile>
 <tile id="28" type="Water" terrain="2,2,2,">
  <properties>
   <property name="collisionInherit" type="int" value="4"/>
  </properties>
 </tile>
 <tile id="29" type="Water" terrain="2,2,,2">
  <properties>
   <property name="collisionInherit" type="int" value="5"/>
  </properties>
 </tile>
 <tile id="30" type="Floor" terrain=",0,," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
  </objectgroup>
 </tile>
 <tile id="31" type="Floor" terrain="0,0,," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="14" width="16" height="2"/>
  </objectgroup>
 </tile>
 <tile id="32" type="Floor" terrain="0,,," probability="25">
  <objectgroup draworder="index">
   <object id="1" x="0" y="14" width="16" height="2"/>
   <object id="2" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="33" probability="25"/>
 <tile id="34" type="Floor" probability="25">
  <objectgroup draworder="index">
   <object id="1" x="14" y="0" width="2" height="2"/>
   <object id="2" x="14" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="35" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="2"/>
   <object id="2" x="0" y="14" width="2" height="2"/>
  </objectgroup>
 </tile>
 <tile id="36" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="2" height="16"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
   <object id="3" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="37" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="0" y="0" width="2" height="16"/>
   <object id="3" x="0" y="14" width="16" height="2"/>
  </objectgroup>
 </tile>
 <tile id="38" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
  </objectgroup>
 </tile>
 <tile id="39" type="Floor">
  <objectgroup draworder="index">
   <object id="1" x="0" y="0" width="16" height="2"/>
   <object id="2" x="0" y="14" width="16" height="2"/>
   <object id="3" x="14" y="0" width="2" height="16"/>
  </objectgroup>
 </tile>
 <tile id="40" type="Water" terrain=",2,,">
  <properties>
   <property name="collisionInherit" type="int" value="30"/>
  </properties>
 </tile>
 <tile id="41" type="Water" terrain="2,2,,">
  <properties>
   <property name="collisionInherit" type="int" value="31"/>
  </properties>
 </tile>
 <tile id="42" type="Water" terrain="2,,,">
  <properties>
   <property name="collisionInherit" type="int" value="32"/>
  </properties>
 </tile>
 <tile id="43" type="Water" terrain="2,,2,2">
  <properties>
   <property name="collisionInherit" type="int" value="19"/>
  </properties>
 </tile>
 <tile id="44" type="Water" terrain=",2,2,2">
  <properties>
   <property name="collisionInherit" type="int" value="20"/>
  </properties>
 </tile>
 <tile id="45" type="Floor" terrain=",,,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="0"/>
  </properties>
 </tile>
 <tile id="46" type="Floor" terrain=",,1,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="1"/>
  </properties>
 </tile>
 <tile id="47" type="Floor" terrain=",,1," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="2"/>
  </properties>
 </tile>
 <tile id="48" type="Floor" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="3"/>
  </properties>
 </tile>
 <tile id="49" type="Floor" terrain="1,1,1," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="4"/>
  </properties>
 </tile>
 <tile id="50" type="Floor" terrain="1,1,,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="5"/>
  </properties>
 </tile>
 <tile id="51" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="6"/>
  </properties>
 </tile>
 <tile id="52" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="7"/>
  </properties>
 </tile>
 <tile id="53" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="8"/>
  </properties>
 </tile>
 <tile id="60" type="Floor" terrain=",1,,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="15"/>
  </properties>
 </tile>
 <tile id="61" type="Floor" terrain="1,1,1,1" probability="5"/>
 <tile id="62" type="Floor" terrain="1,,1," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="17"/>
  </properties>
 </tile>
 <tile id="63" type="Floor" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="18"/>
  </properties>
 </tile>
 <tile id="64" type="Floor" terrain="1,,1,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="19"/>
  </properties>
 </tile>
 <tile id="65" type="Floor" terrain=",1,1,1" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="20"/>
  </properties>
 </tile>
 <tile id="66" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="67" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="22"/>
  </properties>
 </tile>
 <tile id="68" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="23"/>
  </properties>
 </tile>
 <tile id="73" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="38"/>
  </properties>
 </tile>
 <tile id="75" type="Floor" terrain=",1,," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="30"/>
  </properties>
 </tile>
 <tile id="76" type="Floor" terrain="1,1,," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="31"/>
  </properties>
 </tile>
 <tile id="77" type="Floor" terrain="1,,," probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="32"/>
  </properties>
  <objectgroup draworder="index"/>
 </tile>
 <tile id="78" probability="5"/>
 <tile id="79" type="Floor" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="34"/>
  </properties>
 </tile>
 <tile id="80" type="Floor" probability="5">
  <properties>
   <property name="collisionInherit" type="int" value="35"/>
  </properties>
 </tile>
 <tile id="81" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="36"/>
  </properties>
 </tile>
 <tile id="82" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="37"/>
  </properties>
 </tile>
 <tile id="83" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="38"/>
  </properties>
 </tile>
 <tile id="84" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="39"/>
  </properties>
 </tile>
 <tile id="90" type="Floor" terrain=",,,1">
  <properties>
   <property name="collisionInherit" type="int" value="0"/>
  </properties>
 </tile>
 <tile id="91" type="Floor" terrain=",,1,1">
  <properties>
   <property name="collisionInherit" type="int" value="1"/>
  </properties>
 </tile>
 <tile id="92" type="Floor" terrain=",,1,">
  <properties>
   <property name="collisionInherit" type="int" value="2"/>
  </properties>
  <objectgroup draworder="index"/>
 </tile>
 <tile id="93" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="3"/>
  </properties>
 </tile>
 <tile id="94" type="Floor" terrain="1,1,1,">
  <properties>
   <property name="collisionInherit" type="int" value="4"/>
  </properties>
 </tile>
 <tile id="95" type="Floor" terrain="1,1,,1">
  <properties>
   <property name="collisionInherit" type="int" value="5"/>
  </properties>
 </tile>
 <tile id="96" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="6"/>
  </properties>
 </tile>
 <tile id="97" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="7"/>
  </properties>
 </tile>
 <tile id="98" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="8"/>
  </properties>
 </tile>
 <tile id="104" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="105" type="Floor" terrain=",1,,1">
  <properties>
   <property name="collisionInherit" type="int" value="15"/>
  </properties>
 </tile>
 <tile id="106" type="Floor" terrain="1,1,1,1"/>
 <tile id="107" type="Floor" terrain="1,,1,">
  <properties>
   <property name="collisionInherit" type="int" value="17"/>
  </properties>
 </tile>
 <tile id="108" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="18"/>
  </properties>
 </tile>
 <tile id="109" type="Floor" terrain="1,,1,1">
  <properties>
   <property name="collisionInherit" type="int" value="19"/>
  </properties>
 </tile>
 <tile id="110" type="Floor" terrain=",1,1,1">
  <properties>
   <property name="collisionInherit" type="int" value="20"/>
  </properties>
 </tile>
 <tile id="111" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="112" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="22"/>
  </properties>
 </tile>
 <tile id="113" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="23"/>
  </properties>
 </tile>
 <tile id="114" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="24"/>
  </properties>
 </tile>
 <tile id="115" type="Stairs"/>
 <tile id="116" type="Stairs"/>
 <tile id="119" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="120" type="Floor" terrain=",1,,">
  <properties>
   <property name="collisionInherit" type="int" value="30"/>
  </properties>
 </tile>
 <tile id="121" type="Floor" terrain="1,1,,">
  <properties>
   <property name="collisionInherit" type="int" value="31"/>
  </properties>
 </tile>
 <tile id="122" type="Floor" terrain="1,,,">
  <properties>
   <property name="collisionInherit" type="int" value="32"/>
  </properties>
 </tile>
 <tile id="124" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="34"/>
  </properties>
 </tile>
 <tile id="125" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="35"/>
  </properties>
 </tile>
 <tile id="126" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="36"/>
  </properties>
 </tile>
 <tile id="127" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="37"/>
  </properties>
 </tile>
 <tile id="128" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="38"/>
  </properties>
 </tile>
 <tile id="129" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="39"/>
  </properties>
 </tile>
 <tile id="130" type="Stairs"/>
 <tile id="131" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="132" type="Stairs"/>
 <tile id="133" type="Stairs"/>
 <tile id="134" type="Floor">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="141" type="Door"/>
 <tile id="142" type="Door"/>
 <tile id="143" type="Door"/>
 <tile id="145" type="Stairs"/>
 <tile id="146" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="21"/>
  </properties>
 </tile>
 <tile id="147" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="38"/>
  </properties>
 </tile>
 <tile id="148" type="Water">
  <properties>
   <property name="collisionInherit" type="int" value="38"/>
  </properties>
 </tile>
 <tile id="152" type="Floor"/>
 <tile id="156" type="Door"/>
 <tile id="157" type="Door"/>
 <tile id="158" type="Door"/>
 <tile id="160" type="Stairs"/>
</tileset>
