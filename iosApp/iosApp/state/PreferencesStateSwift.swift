//
//  PreferencesStateSwift.swift
//  iosApp
//
//  Created by Roberto García Romero on 10/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

enum PreferencesStateSwift {
    case loading
    case loaded(tracks: [TrackBo])
    case error
}

extension PreferencesStateSwift {
    init?(_ value: PreferencesState) {
        switch value {
        case is PreferencesState.Loading:
            self = .loading
        case let loaded as PreferencesState.Loaded:
            self = .loaded(tracks: loaded.tracks)
        case is PreferencesState.Error:
            self = .error
        default:
            return nil
        }
    }
}

