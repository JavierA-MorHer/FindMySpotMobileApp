package com.example.findmyspot.auth.repository

import com.example.findmyspot.auth.model.User

class UserRepository {
        private val personList = mutableListOf<User>()

        // Agregar una persona
        fun addPerson(person: User) {
            personList.add(person)
        }

        // Borrar una persona
        fun removePerson(person: User) {
            personList.remove(person)
        }



        // Obtener todas las personas
        fun getAllPeople(): List<User> {
            return personList.toList()
        }

}