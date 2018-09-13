package com.mdbank.model.usertypes

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types.ARRAY
import java.util.*

class FloatListUserType : UserType {
    override fun sqlTypes(): IntArray = intArrayOf(ARRAY)

    override fun returnedClass(): Class<*> = ArrayList::class.java

    override fun equals(x: Any, y: Any): Boolean = x == y

    override fun hashCode(x: Any): Int = Objects.hashCode(x)

    override fun nullSafeGet(rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor, owner: Any): Any? {
        val array = rs.getArray(names[0])?.array

        val floats = array as? Array<Float>
        return floats?.asList()
    }

    override fun nullSafeSet(st: PreparedStatement, value: Any?, index: Int, session: SharedSessionContractImplementor) {
        if (value == null) {
            st.setNull(index, ARRAY)
        } else {
            st.connection.createArrayOf("float", (value as List<Float>).toTypedArray())
        }
    }

    override fun deepCopy(value: Any): Any = value

    override fun isMutable(): Boolean = false

    override fun disassemble(value: Any): Serializable = ArrayList(value as List<Float>)

    override fun assemble(cached: Serializable, owner: Any): Any = ArrayList(cached as ArrayList<Float>)

    override fun replace(original: Any, target: Any, owner: Any): Any? = null
}