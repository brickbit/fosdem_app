//
//  ChipView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 1/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ChipView: View {
    let name: String
    
    @State var isSelected: Bool
    
    var onSelectChip: (String) -> Void
    var onDeselectChip: (String) -> Void

    var body: some View {
        HStack(spacing: 4) {
            Text(name).font(.body).lineLimit(1)
            if(isSelected) {
                Image(systemName: "multiply").onTapGesture {
                    onDeselectChip(name)
                    isSelected.toggle()
                }
            }
        }
        .padding(.vertical, 4)
        .padding(.leading, 4)
        .padding(.trailing, 10)
        .foregroundColor(isSelected ? .white : Color("AccentColor"))
        .background(isSelected ? mainGradient : whiteGradient)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(mainGradient, lineWidth: 1.5)
            
        ).onTapGesture {
            if(!isSelected) {
                isSelected.toggle()
            }
            onSelectChip(name)
        }
    }
}

#Preview {
    ChipView(name: "08:00", isSelected: false, onSelectChip: { hour in }, onDeselectChip: { hour in }) 
}
