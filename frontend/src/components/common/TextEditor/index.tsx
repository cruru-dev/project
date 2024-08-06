import ReactQuill from 'react-quill-new';

import { ToolbarOptions } from '@customTypes/textEditor';
import 'react-quill-new/dist/quill.snow.css';
import 'react-quill-new/dist/quill.bubble.css';
import './style.css';

interface TextEditorProps {
  width?: string;
  height?: string;
  toolbarOptions?: ToolbarOptions;
  value: string;
  onChange?: (content: string) => void;
  onBlur?: () => void;
  placeholder?: string;
  theme?: 'snow' | 'bubble';
}

const defaultToolbarOptions: ToolbarOptions = [
  [{ header: [1, 2, 3, false] }],
  ['bold', 'italic', 'underline', 'blockquote', 'link'],
  [{ list: 'ordered' }, { list: 'bullet' }],
  [{ align: [] }, { color: [] }, { background: [] }],
  ['clean'],
];

export default function TextEditor({
  width = '100%',
  height = '100%',
  toolbarOptions = defaultToolbarOptions,
  value,
  onChange,
  onBlur,
  placeholder,
  theme = 'snow',
}: TextEditorProps) {
  const handleChange = (content: string) => {
    if (onChange) {
      onChange(content);
    }
  };

  const handleBlur = () => {
    if (onBlur) {
      onBlur();
    }
  };

  return (
    <ReactQuill
      style={{ width, height }}
      modules={{ toolbar: toolbarOptions }}
      theme={theme}
      readOnly={theme === 'bubble'}
      value={value}
      onChange={handleChange}
      onBlur={handleBlur}
      placeholder={placeholder}
    />
  );
}
