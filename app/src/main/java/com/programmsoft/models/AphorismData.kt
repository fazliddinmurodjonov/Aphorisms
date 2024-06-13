package com.programmsoft.models

data class AphorismData(
    val `data`: List<AphorismItemResponse>,
    val links: Links,
    val meta: Meta
)