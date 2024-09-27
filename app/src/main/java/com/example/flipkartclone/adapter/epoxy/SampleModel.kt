package com.example.flipkartclone.adapter.epoxy

import com.google.android.gms.common.util.CollectionUtils.listOf

data class SampleModel(val id: Int, val type: String, val content: String)

fun getSampleData() = listOf(
    SampleModel(1,"Header", "Category 1"),
    SampleModel(11,"Content", "Hello World"),
    SampleModel(12,"Content", "Lorem-Ipsum"),
    SampleModel(13,"Content", "Hello Android"),
    SampleModel(2,"Header", "Category 2"),
    SampleModel(21,"Content", "Hello World"),
    SampleModel(22,"Content", "Lorem-Ipsum"),
    SampleModel(23,"Content", "Hello Android"),
    SampleModel(3,"Header", "Category 3"),
    SampleModel(31,"Content", "Hello World"),
    SampleModel(32,"Content", "Lorem-Ipsum"),
    SampleModel(33,"Content", "Hello Android"),
    SampleModel(4,"Header", "Category 4"),
    SampleModel(41,"Content", "Hello World"),
    SampleModel(42, "Content", "Lorem-Ipsum"),
    SampleModel(43,"Content", "Hello Android"),
    SampleModel(5,"Footer", "End of list"),
)
