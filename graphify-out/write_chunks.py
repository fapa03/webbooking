import json, re
from pathlib import Path

base = Path(r'c:\Users\practidesarrolloti4\AppData\Roaming\Code\User\workspaceStorage\5bb27dd6ea416466376678e67659027b\GitHub.copilot-chat\chat-session-resources\4723b262-bb8a-45ab-8188-6e371525c5ec')

# Chunk 1: docs
src1 = base / 'toolu_bdrk_01EXg7Ewd6gvXpgwVh7dPTGe__vscode-1779757175453' / 'content.json'
Path('graphify-out/.graphify_chunk_01.json').write_text(src1.read_text(encoding='utf-8'), encoding='utf-8')
print('chunk_01 written')

# Chunk 2: core JS
src2 = base / 'toolu_bdrk_013xme9WNuNxpE1hSKoqbc2c__vscode-1779757175454' / 'content.json'
Path('graphify-out/.graphify_chunk_02.json').write_text(src2.read_text(encoding='utf-8'), encoding='utf-8')
print('chunk_02 written')

# Chunk 3: QZ printing JS
src3 = base / 'toolu_bdrk_016JHJUUFxGJYwWGUAsVs4bi__vscode-1779757175455' / 'content.json'
Path('graphify-out/.graphify_chunk_03.json').write_text(src3.read_text(encoding='utf-8'), encoding='utf-8')
print('chunk_03 written')

# Chunk 4: logos
src4 = base / 'toolu_bdrk_011DJpqgTUQGHUSF9TizQ1Z6__vscode-1779757175456' / 'content.json'
Path('graphify-out/.graphify_chunk_04.json').write_text(src4.read_text(encoding='utf-8'), encoding='utf-8')
print('chunk_04 written')

# Chunk 5: UI images - extract JSON from text
src5_text = (base / 'toolu_bdrk_014bDKf6YS7qJ9EZXfrHRnQs__vscode-1779757175457' / 'content.txt').read_text(encoding='utf-8')
# Find the JSON object
idx = src5_text.find('{"nodes":')
if idx >= 0:
    json_str = src5_text[idx:]
    # validate
    try:
        json.loads(json_str)
        Path('graphify-out/.graphify_chunk_05.json').write_text(json_str, encoding='utf-8')
        print('chunk_05 written')
    except Exception as e:
        print(f'chunk_05 JSON error: {e}')
else:
    print('chunk_05: no JSON found')
