package com.kechinvv.ghsearch

import com.kechinvv.ghsearch.filter.impl.FilterFieldChange.fieldChange
import com.kechinvv.ghsearch.filter.impl.FilterRecursion
import com.kechinvv.ghsearch.repository.RepIterator
import com.kechinvv.ghsearch.repository.link.OrderType
import com.kechinvv.ghsearch.repository.link.SortType
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import java.nio.file.Paths


fun main(args: Array<String>) {
    if (args.isEmpty()) throw IllegalArgumentException("Not enough args")
    RepIterator(
        100,
        "kotlin",
        SortType.STARS,
        OrderType.DESC
    ).forEach { repository ->
        val localRep = repository.cloneTo(Paths.get(args[0], repository.name))
        val stream = localRep.ktStream
        stream.forEach { x ->
            val creator = PSICreator()
            val psi = creator.getPSIForFile(x)
            val ctx = creator.getBinding()
            val recursionFunctions = FilterRecursion.require(psi, ctx!!)
            val fields: MutableList<PropertyDescriptor> = mutableListOf()
            recursionFunctions.forEach {
                val a = it.fieldChange(ctx)
                if (a.isNotEmpty()) fields += a
            }
            if (fields.isEmpty()) localRep.delete()
        }
    }
}
