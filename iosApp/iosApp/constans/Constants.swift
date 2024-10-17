//
//  Constants.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 3/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

let mainGradient = LinearGradient(
    gradient: Gradient(
        colors: [
            Color("AccentColor"),
            Color("AccentColorDark"),
            Color("AccentColorExtraDark")
        ]
    ),
    startPoint: .leading,
    endPoint: .trailing
)

let whiteGradient = LinearGradient(
    gradient: Gradient(
        colors: [
            .white,
            .white,
            .white
        ]
    ),
    startPoint: .leading,
    endPoint: .trailing
)

let blackGradient = LinearGradient(
    gradient: Gradient(
        colors: [
            .black,
            .black,
            .black
        ]
    ),
    startPoint: .leading,
    endPoint: .trailing
)
