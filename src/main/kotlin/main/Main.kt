package main

import com.spbpu.mppconverter.kootstrap.PSICreator
import main.filter.impl.FilterFieldChange.fieldChange
import main.filter.impl.FilterRecursion
import main.link.OrderType
import main.link.SortType
import org.jetbrains.kotlin.descriptors.PropertyDescriptor


fun main(args: Array<String>) {
    val iterator = RepIterator(100, "kotlin", SortType.STARS, OrderType.DESC)
    while (iterator.hasNext()) {
        val remRep = iterator.next()
        val localRep = remRep.cloneTo("C:/Users/valer/IdeaProjects/GithubSearch/zips/" + remRep.name)
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
