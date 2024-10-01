//
//  LanguageView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct LanguageView: View {
    var body: some View {
        VStack(alignment: .leading) {
            Text("Language")
                .font(.title)
                .padding(
                    EdgeInsets(
                        top: 0, leading: 20, bottom: 0, trailing: 0))
            List {
                HStack{
                    Text("English")
                    Spacer()
                    Image(systemName: "chevron.right")
                }.onTapGesture {}
                HStack{
                    Text("Español")
                    Spacer()
                    Image(systemName: "chevron.right")
                }.onTapGesture {}
                HStack{
                    Text("Français")
                    Spacer()
                    Image(systemName: "chevron.right")
                }.onTapGesture {}
            }.listStyle(.inset)
        }
    }
}

#Preview {
    LanguageView()
}
