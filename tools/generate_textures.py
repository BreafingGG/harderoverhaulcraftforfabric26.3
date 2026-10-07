#!/usr/bin/env python3
"""Generates every 16x16 texture used by HarderOverhaulCraft.

Usage: python3 tools/generate_textures.py [path/to/minecraft-client.jar]

The vanilla client jar (downloaded by Loom into ~/.gradle/caches/fabric-loom/<mc>/)
is only needed for the gold-spotted upgraded tool textures and the diamond nugget
silhouette; all other textures are drawn from scratch. The generated PNGs are
committed, so the Gradle build itself never runs this script.
"""
import io
import os
import random
import sys
import zipfile

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TEX = os.path.join(ROOT, "src/main/resources/assets/harderoverhaulcraft/textures")
DEFAULT_JAR = os.path.expanduser("~/.gradle/caches/fabric-loom/26.3/minecraft-client.jar")


def hx(c, a=255):
    c = c.lstrip("#")
    return (int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16), a)


def save(img, rel):
    path = os.path.join(TEX, rel)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)
    print("wrote", os.path.relpath(path, ROOT))


def new():
    return Image.new("RGBA", (16, 16), (0, 0, 0, 0))


def lum(px):
    return (0.299 * px[0] + 0.587 * px[1] + 0.114 * px[2]) / 255.0


def recolor(src, palette):
    """Maps the luminance of every opaque pixel onto a dark->light palette."""
    out = new()
    for y in range(16):
        for x in range(16):
            p = src.getpixel((x, y))
            if p[3] == 0:
                continue
            idx = min(len(palette) - 1, int(lum(p) * len(palette)))
            out.putpixel((x, y), palette[idx])
    return out


# ---------------------------------------------------------------- items

def diamond_nugget(jar):
    palette = [hx("#0B3D5C"), hx("#00799E"), hx("#00BFFF"), hx("#4FC3F7"), hx("#B2EBF2"), hx("#FFFFFF")]
    if jar:
        img = recolor(jar_image(jar, "assets/minecraft/textures/item/gold_nugget.png"), palette)
    else:
        img = new()
        shape = ["......XX......", ".....XXXX.....", "....XXXXXX....", "....XXXXXX....", ".....XXXX.....", "......XX......"]
        for dy, row in enumerate(shape):
            for dx, ch in enumerate(row):
                if ch == "X":
                    img.putpixel((dx + 1, dy + 5), palette[2 + (dx + dy) % 3])
    # glint highlight in the upper-left of the gem
    opaque = [(x, y) for y in range(16) for x in range(16) if img.getpixel((x, y))[3]]
    if opaque:
        minx = min(p[0] for p in opaque)
        miny = min(p[1] for p in opaque)
        for gx, gy in ((minx + 1, miny + 1), (minx + 2, miny + 1), (minx + 1, miny + 2)):
            if img.getpixel((gx, gy))[3]:
                img.putpixel((gx, gy), hx("#FFFFFF"))
    save(img, "item/diamond_nugget.png")


def rock_shard():
    dark, mid, light, hi = hx("#424242"), hx("#757575"), hx("#9E9E9E"), hx("#BDBDBD")
    shape = [
        "................",
        "..........X.....",
        ".........XX.....",
        "........XXXX....",
        ".......XXXXX....",
        "......XXXXXXX...",
        ".....XXXXXXXX...",
        "....XXXXXXXXX...",
        "...XXXXXXXXXX...",
        "...XXXXXXXXX....",
        "..XXXXXXXXXX....",
        "..XXXXXXXXX.....",
        "...XXXXXXX......",
        "....XXXXX.......",
        ".....XX.........",
        "................",
    ]
    img = new()
    rnd = random.Random(7)
    for y, row in enumerate(shape):
        for x, ch in enumerate(row):
            if ch != "X":
                continue
            left = x == 0 or row[x - 1] != "X"
            top = y == 0 or shape[y - 1][x] != "X"
            right = x == 15 or row[x + 1] != "X"
            bottom = y == 15 or shape[y + 1][x] != "X"
            if right or bottom:
                c = dark
            elif left or top:
                c = hi
            else:
                c = rnd.choice([mid, mid, light])
            img.putpixel((x, y), c)
    # a fracture line across the shard
    for x, y in ((5, 10), (6, 9), (7, 9), (8, 8), (9, 7)):
        img.putpixel((x, y), dark)
    save(img, "item/rock_shard.png")


def gold_spotted_tools(jar):
    gold_hi, gold = hx("#FFF176"), hx("#FDD835")
    tiers = {"gold_reinforced_iron": "iron", "diamond_plus": "diamond", "netherite_plus": "netherite"}
    tools = ["pickaxe", "axe", "sword", "shovel", "hoe"]
    for prefix, base in tiers.items():
        for tool in tools:
            img = jar_image(jar, f"assets/minecraft/textures/item/{base}_{tool}.png").copy()
            # the tool "head" occupies the upper-right half of every vanilla tool sprite;
            # pick the material (non-handle) pixels there and sprinkle 5 gold spots
            candidates = []
            for y in range(16):
                for x in range(16):
                    p = img.getpixel((x, y))
                    if p[3] == 0 or x + (15 - y) < 15:
                        continue
                    r, g, b = p[:3]
                    is_handle = r > g + 25 and r > b + 25 and lum(p) < 0.55  # brown stick
                    if not is_handle:
                        candidates.append((x, y))
            rnd = random.Random(sum(map(ord, prefix + tool)))
            rnd.shuffle(candidates)
            chosen = []
            for c in candidates:
                if all(abs(c[0] - o[0]) + abs(c[1] - o[1]) > 2 for o in chosen):
                    chosen.append(c)
                if len(chosen) == 6:
                    break
            for i, (x, y) in enumerate(chosen):
                img.putpixel((x, y), gold_hi if i % 2 == 0 else gold)
            save(img, f"item/{prefix}_{tool}.png")


# ---------------------------------------------------------------- blocks

def noise_fill(img, palette, seed, x0=0, y0=0, x1=16, y1=16):
    rnd = random.Random(seed)
    for y in range(y0, y1):
        for x in range(x0, x1):
            img.putpixel((x, y), rnd.choice(palette))


def compressed_cobblestone():
    # tight, regular 4x4 grid of small "cobbles" - darker than vanilla cobblestone
    mortar, dark, mid, light = hx("#2E2E2E"), hx("#4A4A4A"), hx("#5F5F5F"), hx("#787878")
    img = new()
    rnd = random.Random(11)
    for by in range(4):
        for bx in range(4):
            off = 2 if by % 2 else 0
            for y in range(by * 4, by * 4 + 4):
                for x in range(16):
                    lx = (x + off) % 4
                    ly = y - by * 4
                    if (x + off) // 4 % 4 != bx:
                        continue
                    if lx == 3 or ly == 3:
                        c = mortar
                    elif lx == 0 or ly == 0:
                        c = light
                    else:
                        c = rnd.choice([dark, mid, mid])
                    img.putpixel((x, y), c)
    save(img, "block/compressed_cobblestone.png")


STONE = [hx("#6B6B6B"), hx("#7A7A7A"), hx("#858585"), hx("#8F8F8F")]


def framed_stone(seed):
    img = new()
    noise_fill(img, STONE, seed)
    for i in range(16):
        for (x, y) in ((i, 0), (0, i)):
            img.putpixel((x, y), hx("#9A9A9A"))
        for (x, y) in ((i, 15), (15, i)):
            img.putpixel((x, y), hx("#4E4E4E"))
    return img


def rock_shaper():
    # top: bowl-shaped depression
    top = framed_stone(21)
    for y in range(16):
        for x in range(16):
            d = ((x - 7.5) ** 2 + (y - 7.5) ** 2) ** 0.5
            if d < 5.5:
                shade = int(40 + d * 9)
                top.putpixel((x, y), (shade, shade, shade, 255))
            elif d < 6.5:
                top.putpixel((x, y), hx("#A8A8A8"))
    save(top, "block/rock_shaper_top.png")
    # side: dark input slot with a lip
    side = framed_stone(22)
    for y in range(5, 10):
        for x in range(4, 12):
            side.putpixel((x, y), hx("#1E1E1E") if 5 < y < 9 and 4 < x < 11 else hx("#3A3A3A"))
    for x in range(3, 13):
        side.putpixel((x, 10), hx("#A8A8A8"))
    for x in range(5, 11):
        side.putpixel((x, 12), hx("#5A4632"))  # small gear/marker bar
    save(side, "block/rock_shaper_side.png")
    save(framed_stone(23), "block/rock_shaper_bottom.png")


def beacon(name, base_pal, trim, core, core_hi, seed):
    side = new()
    noise_fill(side, base_pal, seed)
    for i in range(16):
        for p in ((i, 0), (i, 15), (0, i), (15, i)):
            side.putpixel(p, trim)
    for y in range(5, 11):
        for x in range(5, 11):
            side.putpixel((x, y), core if 6 <= x <= 9 and 6 <= y <= 9 else trim)
    for p in ((7, 7), (8, 8)):
        side.putpixel(p, core_hi)
    save(side, f"block/{name}_side.png")

    top = new()
    noise_fill(top, base_pal, seed + 1)
    for i in range(16):
        for p in ((i, 0), (i, 15), (0, i), (15, i)):
            top.putpixel(p, trim)
    for y in range(16):
        for x in range(16):
            d = ((x - 7.5) ** 2 + (y - 7.5) ** 2) ** 0.5
            if d < 2.2:
                top.putpixel((x, y), core_hi)
            elif d < 3.6:
                top.putpixel((x, y), core)
            elif d < 4.4:
                top.putpixel((x, y), trim)
    save(top, f"block/{name}_top.png")

    bottom = new()
    noise_fill(bottom, base_pal, seed + 2)
    save(bottom, f"block/{name}_bottom.png")


def jar_image(jar, member):
    with zipfile.ZipFile(jar) as z:
        return Image.open(io.BytesIO(z.read(member))).convert("RGBA")


def main():
    jar = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_JAR
    if not os.path.exists(jar):
        sys.exit(f"Minecraft client jar not found at {jar} (run ./gradlew genSources first)")
    diamond_nugget(jar)
    rock_shard()
    gold_spotted_tools(jar)
    compressed_cobblestone()
    rock_shaper()
    beacon("crude_beacon",
           [hx("#3B3632"), hx("#45403A"), hx("#4F4842"), hx("#3F3A35")],
           hx("#2A2622"), hx("#E0B341"), hx("#FFF3A0"), 31)
    beacon("refined_beacon",
           [hx("#6E6A66"), hx("#7C7772"), hx("#86817B"), hx("#74706B")],
           hx("#E6B422"), hx("#7FE3FF"), hx("#FFFFFF"), 41)


if __name__ == "__main__":
    main()
