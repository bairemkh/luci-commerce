package com.example.lucidelivery.domain.model

enum class AccountType { USER, BUSINESS }

data class Session(val token: String, val accountType: AccountType, val accountId: String)

