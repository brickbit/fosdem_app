//
//  SplashStateSwift.swift
//  iosApp
//
//  Created by Roberto García Romero on 8/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

enum SplashStateSwift {
    case initialized
    case finished
    case error
}

extension SplashStateSwift {
    init?(_ value: SplashState) {
        switch value {
        case is SplashState.Init:
            self = .initialized
        case is SplashState.Finished:
            self = .finished
        case is SplashState.Error:
            self = .error
        default:
            return nil
        }
    }
}
