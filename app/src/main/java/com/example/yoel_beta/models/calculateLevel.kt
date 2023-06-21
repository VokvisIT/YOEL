package com.example.yoel_beta.models


fun calculateLevelInfo(experience: Int): Triple<Int, Int, Int> { // массив значений необходимого опыта для каждого уровня
    val experiencePerLevel = arrayOf(125, 250, 500, 750, 1000, 1250, 1500, 1750, 2000)

    var currentLevel = 0 // начальный уровень

    for (i in experiencePerLevel.indices) { // проходим по массиву значений опыта для каждого уровня
        if (experience < experiencePerLevel[i]) { // если общий опыт меньше или равен текущему значению в массиве
            currentLevel = i + 1 // текущий уровень равен индексу текущего значения в массиве плюс один
            break // выходим из цикла
        }
    }
    if (currentLevel == 0) { // если общий опыт меньше необходимого для первого уровня
        currentLevel = 1 // текущий уровень равен 1
    } else if (currentLevel == experiencePerLevel.size + 1) { // если общий опыт больше последнего значения в массиве
        currentLevel += 1 // текущий уровень равен количеству элементов в массиве плюс один
    }
    val experienceToNextLevel = experiencePerLevel.getOrElse(currentLevel - 1) { 0 } - experiencePerLevel.getOrElse(currentLevel - 2) { 0 }
    val experienceFromLevel = experience - experiencePerLevel.getOrElse(currentLevel - 2) { 0 }

    return Triple(currentLevel, experienceToNextLevel, experienceFromLevel)
}
