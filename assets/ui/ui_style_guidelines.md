# OTAMS– UI Style Guidelines

## 🧭 Overview
This document defines the **visual identity** and **design standards** for the *otams* mobile app.
It ensures a consistent look and feel across all screens, components, and future updates.

The style guide includes the **color palette**, **typography system**, and **layout spacing rules** used throughout the app.

The overall look and feel should be **modern, calm, and professional**, reflecting the academic purpose of the app.

---

## 🌈 Color Palette

| Color Name | Hex | Usage |
|-------------|------|--------|
| **Dark Gunmetal** | `#242528` | Primary text, navbar, app bar background, cards |
| **Deep Dumpling** | `#9E3120` | Accent color — buttons, icons, highlights |
| **Pale Silver** | `#D5C4BB` | Background or surfaces (light mode) |
| **Cadet Grey** | `#98A6A9` | Secondary text, placeholders, disabled states |
| **Light Slate Gray** | `#798F9C` | Borders, dividers, secondary buttons |

![Color Palette](//assets/ui/color_palette.png)

> All colors are declared in `res/values/colors.xml` for easy global access.

---

## ✍️ Typography

| Text Style | Font | Weight | Size | Color | Example Use |
|-------------|------|--------|------|--------|--------------|
| **H1** | Poppins | Bold | 28sp | Dark Gunmetal | Screen titles |
| **H2** | Poppins | SemiBold | 22sp | Dark Gunmetal | Section titles |
| **H3** | Poppins | Medium | 18sp | Deep Dumpling | Subtitles / card titles |
| **Body 1** | Roboto | Regular | 16sp | Dark Gunmetal | Main body text |
| **Body 2** | Roboto | Regular | 14sp | Cadet Grey | Secondary text |
| **Caption** | Roboto | Light | 12sp | Light Slate Gray | Labels / small notes |
| **Button** | Poppins | SemiBold | 16sp | Pale Silver | Primary buttons |

---

## 📏 Spacing & Layout Rules
- Use **16dp** margin between sections.
- Titles have **24dp** top margin.
- Buttons have **8dp corner radius** and full width.
- Maintain line spacing of **1.3× font size** for readability.
- Cards have **4dp elevation** for subtle depth.

---

## 🧩 Implementation Notes
In `themes.xml`, set the default font:
```xml
<item name="android:fontFamily">@font/roboto</item>
```

In `styles.xml`, create reusable text styles:
```xml
<style name="Heading1">
    <item name="android:fontFamily">@font/poppins_bold</item>
    <item name="android:textSize">28sp</item>
    <item name="android:textColor">@color/dark_gunmetal</item>
</style>
```

Then apply:
```xml
<TextView
    style="@style/Heading1"
    android:text="Dashboard" />
```
